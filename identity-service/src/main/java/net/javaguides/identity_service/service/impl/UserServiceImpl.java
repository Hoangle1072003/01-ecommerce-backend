package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.User;

import net.javaguides.identity_service.domain.request.ReqUserGoogleDto;
import net.javaguides.identity_service.domain.response.ResMeta;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.service.IRoleService;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.SecurityUtil;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User handleGetUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
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
        User user = userRepository.findByEmail(email);
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
        if (oldUser != null) {
            return oldUser;
        }
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


//    @Override
//    public boolean verifyGoogleAccessToken(String token) {
//        RestTemplate restTemplate = new RestTemplate();
//        String introspectionUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token;
//
//        try {
//            ResponseEntity<Map> response = restTemplate.getForEntity(introspectionUrl, Map.class);
//            return response.getStatusCode().is2xxSuccessful();
//        } catch (Exception e) {
//            return false;
//        }
//    }


}
