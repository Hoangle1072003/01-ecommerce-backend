package net.javaguides.identity_service.service.impl;

import jakarta.persistence.criteria.From;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.event.dto.*;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.User;

import net.javaguides.identity_service.domain.request.*;
import net.javaguides.identity_service.domain.response.*;
import net.javaguides.identity_service.mapper.IUserMapper;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.service.IOTPService;
import net.javaguides.identity_service.service.IRoleService;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.service.httpClient.ICartServiceClient;
import net.javaguides.identity_service.service.httpClient.IOrderServiceClient;
import net.javaguides.identity_service.utils.SecurityUtil;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import net.javaguides.identity_service.utils.constant.CartStatusEnum;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * File: UserServiceImpl.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:58 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final IOTPService otpService;
    private final ICartServiceClient cartServiceClient;
    private final IOrderServiceClient orderServiceClient;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, UserForgotPasswordEvent> userForgotPasswordEventKafkaTemplate;
    private final KafkaTemplate<String, UserCancelAccountEvent> userCancelAccountEventKafkaTemplate;
    private final KafkaTemplate<String, UserCancelAccountSuccessEvent> userCancelAccountSuccessEventKafkaTemplate;
    private final KafkaTemplate<String, UserActiveSuspendEvent> userActiveSuspendEventKafkaTemplate;
    private static final String USER_FORGOT_PASSWORD_TOPIC = "USER_FORGOT_PASSWORD_TOPIC";
    private static final String USER_CANCEL_ACCOUNT_TOPIC = "USER_CANCEL_ACCOUNT_TOPIC";
    private static final String USER_CANCEL_ACCOUNT_SUCCESS_TOPIC = "USER_CANCEL_ACCOUNT_SUCCESS_TOPIC";
    private static final String USER_ACTIVE_SUSPEND_TOPIC = "USER_ACTIVE_SUSPEND_TOPIC";

    @Override
    public User handleUser(User user) {
        if (user.getRole() == null) {
            user.setProvider(AuthProvider.LOCAL);
            Role userRole = roleRepository.findByName("USER");
            user.setRole(userRole);
        }
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if ("admin@gmail.com".equals(user.getEmail())) {
            throw new RuntimeException("Cannot delete admin user");
        }

        userRepository.deleteById(id);
    }

    @Override
    public ResResultPaginationDTO getAllUsers(Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(pageable);
        return new ResResultPaginationDTO(
                new ResMeta(
                        pageUsers.getNumber() + 1,
                        pageUsers.getSize(),
                        pageUsers.getTotalPages(),
                        pageUsers.getTotalElements()
                ),
                pageUsers.getContent()
        );
    }

    @Override
    public ResResultPaginationDTO getAllUsersIsNull(Pageable pageable) {
        Page<User> pageUsers = userRepository.findAllByRoleName(pageable, "USER");
        return new ResResultPaginationDTO(
                new ResMeta(
                        pageUsers.getNumber() + 1,
                        pageUsers.getSize(),
                        pageUsers.getTotalPages(),
                        pageUsers.getTotalElements()
                ),
                pageUsers.getContent()
        );
    }

    @Override
    public List<User> findByUserIsNullNameOrEmail(String keyword) {
        return userRepository.searchUsersByRoleNameIsUser(keyword);
    }

    @Override
    public User handleUpdateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        existingUser.setPassword(encodedPassword);
        existingUser.setAge(user.getAge());

        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setGender(user.getGender());
        existingUser.setStatus(user.getStatus());

        if (user.getRole() != null) {
            Role role = roleService.fetchById(user.getRole().getId());
            existingUser.setRole(role != null ? role : null);
        }
        log.info("User updated successfully");
        return userRepository.save(existingUser);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmailAndProvider(email, AuthProvider.LOCAL);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }


    @Override
    public User handleGetUserByUserName(String userName) {
        User user = userRepository.findByEmailAndProvider(userName, AuthProvider.LOCAL)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    @Override
    public User handleGetUserByUserNameByLocal(String userName, AuthProvider authProvider) {
        return userRepository.findByEmailAndProvider(userName, authProvider).orElseThrow();
    }

    @Override
    public void updateUserToken(String email, String token) {
        User user = handleGetUserByUserName(email);
        if (user != null) {
            user.setRefreshToken(token);
            log.info("User updated successfully");
            userRepository.save(user);
        }
        log.info("User not found");
    }

    @Override
    public User getUserByRefreshToken(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

    @Override
    public List<User> findByNameOrActive(String name, StatusEnum status, UUID roleId) {
        return userRepository.findByNameOrEmailOrStatusOrRoleId(name, status, roleId);
    }

    @Override
    public boolean activateUserByEmail(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProvider.LOCAL).orElse(null);
        if (user != null && user.getStatus().equals(StatusEnum.PENDING_ACTIVATION)) {
            user.setStatus(StatusEnum.ACTIVATED);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User saveUserByGoogle(ReqUserGoogleDto reqUserGoogleDto) {
        User oldUser = userRepository.findByEmail(reqUserGoogleDto.getEmail());

        if (oldUser != null && oldUser.getProvider().equals(AuthProvider.LOCAL)) {
            User newUser = new User();
            newUser.setEmail(reqUserGoogleDto.getEmail());
            newUser.setName(reqUserGoogleDto.getName());
            newUser.setImageUrl(reqUserGoogleDto.getPicture());
            newUser.setProviderId(reqUserGoogleDto.getSub());
            newUser.setStatus(StatusEnum.ACTIVATED);
            newUser.setProvider(AuthProvider.GOOGLE);

            Role userRole = roleRepository.findByName("USER");
            newUser.setRole(userRole);

            return userRepository.save(newUser);
        }

        if (oldUser == null) {
            User user = new User();
            user.setEmail(reqUserGoogleDto.getEmail());
            user.setName(reqUserGoogleDto.getName());
            user.setImageUrl(reqUserGoogleDto.getPicture());
            user.setProviderId(reqUserGoogleDto.getSub());
            user.setStatus(StatusEnum.ACTIVATED);
            user.setProvider(AuthProvider.GOOGLE);

            Role userRole = roleRepository.findByName("USER");
            user.setRole(userRole);

            return userRepository.save(user);
        }

        return oldUser;
    }


    @Override
    public User saveUserByGithub(ReqUserGoogleDto reqUserGoogleDto) {
        User oldUser = userRepository.findByEmail(reqUserGoogleDto.getEmail());
        if (oldUser != null) {
            return oldUser;
        }
        User user = new User();

        user.setEmail(reqUserGoogleDto.getEmail());
        user.setName(reqUserGoogleDto.getName());
        user.setImageUrl(reqUserGoogleDto.getPicture());
        user.setProviderId(reqUserGoogleDto.getSub());
        user.setStatus(StatusEnum.ACTIVATED);
        user.setProvider(AuthProvider.GITHUB);
        Role userRole = roleRepository.findByName("USER");
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @Override
    public String resetPassword(User user) {
        String token = securityUtil.createActivationToken(user.getEmail());
        userForgotPasswordEventKafkaTemplate.send(USER_FORGOT_PASSWORD_TOPIC, new UserForgotPasswordEvent(user.getEmail(), token));
        return "Reset password link has been sent to your email";
    }

    @Override
    public Void resetPasswordConfirm(ReqResetPasswordDto reqResetPasswordDto) {
        User user = userRepository.findById(UUID.fromString(reqResetPasswordDto.getId())).orElseThrow();
        if (reqResetPasswordDto.getPassword().equals(reqResetPasswordDto.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(reqResetPasswordDto.getPassword()));
            userRepository.save(user);
            return null;
        }
        return null;
    }

    @Override
    public ResUpdateUserDto updateUserClient(ReqUpdateUserDto reqUpdateUserDto) {
        User user = userRepository.findById(reqUpdateUserDto.getId()).orElseThrow();
        user.setName(reqUpdateUserDto.getName());
        user.setAddress(reqUpdateUserDto.getAddress());
        user.setGender(reqUpdateUserDto.getGender());
        return userMapper.convertToResUpdateUserDto(userRepository.save(user));
    }

    @Override
    public ResUpdateUserPhoneDto updateUserPhone(ReqUpdateUserPhoneDto reqUpdateUserPhoneDto) {
        User user = userRepository.findById(reqUpdateUserPhoneDto.getId()).orElseThrow();
        user.setPhone(reqUpdateUserPhoneDto.getPhone());
        return userMapper.convertToResUpdateUserPhoneDto(userRepository.save(user));
    }

    public Void cancelAccount(ReqCancelAccountDto reqCancelAccountDto) {
        User user = userRepository.findByEmailAndProvider(reqCancelAccountDto.getEmail(), reqCancelAccountDto.getProvider()).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User không tồn tại.");
        }

        ResCartByUser resCartByUser = cartServiceClient.getCartByUserId(user.getId());
        List<ResAllOrderByUserIdDto> resAllOrderByUserIdDto = orderServiceClient.getAllOrdersByUserIdClient(user.getId().toString());

        System.out.println("resCartByUser: " + resCartByUser);
        System.out.println("resAllOrderByUserIdDto: " + resAllOrderByUserIdDto);

        boolean hasActiveCart = resCartByUser != null && CartStatusEnum.ACTIVE.equals(resCartByUser.getStatus());
        boolean hasActiveOrder = resAllOrderByUserIdDto != null && resAllOrderByUserIdDto.stream()
                .anyMatch(order -> StatusEnum.ACTIVATED.equals(order.getOrderStatusEnum()));

        if (hasActiveCart || hasActiveOrder) {
            throw new RuntimeException("Không thể hủy tài khoản vì bạn còn hàng trong giỏ hoặc đơn hàng đang hoạt động.");
        }

        String redisKey = "otp:user:" + user.getEmail();
        String existingOtp = redisTemplate.opsForValue().get(redisKey);

        if (existingOtp != null) {
            throw new RuntimeException("Bạn đã yêu cầu OTP trước đó. Vui lòng kiểm tra email hoặc thử lại sau.");
        }

        String otp = otpService.generateOtp(user.getEmail());

        userCancelAccountEventKafkaTemplate.send(USER_CANCEL_ACCOUNT_TOPIC,
                new UserCancelAccountEvent(user.getEmail(), reqCancelAccountDto.getReason(), otp));

        return null;
    }


    @Override
    public Void cancelAccountOTP(ReqCancelDto reqCancelDto) {
        User user = userRepository.findByEmailAndProvider(reqCancelDto.getEmail(), reqCancelDto.getProvider()).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User không tồn tại.");
        }

        String redisKey = "otp:user:" + user.getEmail();
        String existingOtp = redisTemplate.opsForValue().get(redisKey);
        String redisOtp = redisTemplate.opsForValue().get(redisKey);

        if (existingOtp == null) {
            throw new RuntimeException("OTP không tồn tại hoặc đã hết hạn.");
        }

        boolean isOtpValid = otpService.verifyOtp(user.getEmail(), redisOtp);
        if (!isOtpValid) {
            throw new RuntimeException("OTP không hợp lệ.");
        }

        user.setIsDeleted(true);
        user.setStatus(StatusEnum.DEACTIVATED);
        user.setReason(reqCancelDto.getReason());
        redisTemplate.delete(redisKey);
        userRepository.save(user);

        userCancelAccountSuccessEventKafkaTemplate.send(USER_CANCEL_ACCOUNT_SUCCESS_TOPIC,
                new UserCancelAccountSuccessEvent(LocalDateTime.now(), user.getEmail(), reqCancelDto.getReason()));
        return null;
    }

    @Override
    public Void suspendAccount(ReqSuspendAccountDto reqSuspendAccountDto) {
        User user = userRepository.findByEmailAndProvider(reqSuspendAccountDto.getEmail(), reqSuspendAccountDto.getProvider()).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User không tồn tại.");
        }
        if (!passwordEncoder.matches(reqSuspendAccountDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác.");
        }
        user.setStatus(StatusEnum.SUSPENDED);
        userRepository.save(user);
        return null;
    }

    @Override
    public Void activeAccountSuspend(ReqActiveAccountSuspendDto reqSuspendDto) {
        User user = userRepository.findByEmailAndProvider(reqSuspendDto.getEmail(), AuthProvider.LOCAL).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User không tồn tại.");
        }
        String otp = otpService.generateOtp(user.getEmail());
        userActiveSuspendEventKafkaTemplate.send(USER_ACTIVE_SUSPEND_TOPIC,
                new UserActiveSuspendEvent(user.getEmail(), otp));
        return null;
    }

    @Override
    public Void activeAccountSuspendOTP(ReqActiveAccountSuspendOTPDto reqActiveAccountSuspendOTPDto) {
        User user = userRepository.findByEmailAndProvider(reqActiveAccountSuspendOTPDto.getEmail(), AuthProvider.LOCAL).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User không tồn tại.");
        }
        String redisKey = "otp:user:" + user.getEmail();
        String existingOtp = redisTemplate.opsForValue().get(redisKey);
        if (existingOtp == null) {
            throw new RuntimeException("OTP không tồn tại hoặc đã hết hạn.");
        }
        boolean isOtpValid = otpService.verifyOtp(user.getEmail(), existingOtp);
        if (!isOtpValid) {
            throw new RuntimeException("OTP không hợp lệ.");
        }
        user.setStatus(StatusEnum.ACTIVATED);
        userRepository.save(user);
        redisTemplate.delete(redisKey);
        return null;
    }

    @Override
    public Object checkAccountSuspend(ReqCheckAccountSuspendDto reqCheckAccountSuspendDto) {
        List<User> users = userRepository.findUsersByEmail(reqCheckAccountSuspendDto.getEmail());

        if (users.isEmpty()) {
            throw new RuntimeException("User không tồn tại.");
        }

        List<User> suspendedUsers = users.stream()
                .filter(user -> user.getStatus() == StatusEnum.SUSPENDED)
                .collect(Collectors.toList());

        if (suspendedUsers.isEmpty()) {
            throw new RuntimeException("Không có tài khoản nào bị suspend.");
        }

        return suspendedUsers.size() == 1 ? suspendedUsers.get(0) : suspendedUsers;
    }

}
