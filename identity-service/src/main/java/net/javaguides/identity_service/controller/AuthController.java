package net.javaguides.identity_service.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.event.dto.UserActiveEvent;
import net.javaguides.event.dto.UserActiveSuspendEvent;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.*;
import net.javaguides.identity_service.domain.response.ResCreateUserDTO;
import net.javaguides.identity_service.domain.response.ResLoginDTO;
import net.javaguides.identity_service.domain.response.ResResendActivationDto;
import net.javaguides.identity_service.mapper.IUserCreateMapper;
import net.javaguides.identity_service.mapper.IUserMapper;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.SecurityUtil;
import net.javaguides.identity_service.utils.annotation.ApiMessage;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import net.javaguides.identity_service.utils.error.AccountDeletedException;
import net.javaguides.identity_service.utils.error.AccountNotActivatedException;
import net.javaguides.identity_service.utils.error.AccountNotSuspendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * File: AuthController.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:23 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Slf4j
@RestController()
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IUserMapper userMapper;
    private final IUserCreateMapper userCreateMapper;
    @Value("${spring.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;
    @Value("${spring.security.authentication.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    private final KafkaTemplate<String, UserActiveEvent> userActiveEventKafkaTemplate;
    private final KafkaTemplate<String, UserActiveSuspendEvent> userRegisterEventKafkaTemplate;
    private static final String USER_ACTIVE_TOPIC = "USER_ACTIVE_ACCOUNT";
    private static final String USER_REGISTER_TOPIC = "USER_REGISTER_ACCOUNT";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;


    @PostMapping("/introspect")
    public ResponseEntity<Boolean> introspect(@RequestBody TokenIntrospectionRequest tokenRequest) {
        String token = tokenRequest.getToken();
        try {
            Jwt jwt = securityUtil.checkValidJWTAccessToken(token);
            String email = jwt.getSubject();
            User user = userService.handleGetUserByUserName(email);

            if (user != null && user.getStatus().equals(StatusEnum.ACTIVATED)) {
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            boolean isGoogleIdToken = validateGoogleIdToken(token);
            if (isGoogleIdToken) {
                return ResponseEntity.ok(true);
            } else {
                boolean isGithubAccessToken = validateGithubAccessToken(token);
                if (isGithubAccessToken) {
                    return ResponseEntity.ok(true);
                }
            }
            log.error("JWT Verification Failed: {}", e.getMessage());
        }

        return ResponseEntity.ok(false);
    }


    public boolean validateGoogleIdToken(String idToken) {
        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String email = (String) response.getBody().get("email");
                if (email != null) {
                    log.info(">>> Google ID Token is valid for email: " + email);
                    return true;
                }
            }

        } catch (Exception e) {
            log.warn(">>> Google ID Token validation failed: " + e.getMessage());
        }
        return false;
    }

    public boolean validateGithubAccessToken(String accessToken) {
        try {
            String url = "https://api.github.com/user";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String login = (String) response.getBody().get("login");
                if (login != null) {
                    log.info(">>> GitHub OAuth Access Token is valid for user: " + login);
                    return true;
                }
            } else {
                log.error("GitHub API error: {}", response.getBody());
            }
        } catch (Exception e) {
            log.warn(">>> GitHub OAuth Access Token validation failed: " + e.getMessage());
        }
        return false;
    }


    public boolean validateGoogleAccessToken(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String email = (String) response.getBody().get("email");
                if (email != null) {
                    log.info(">>> Google Access Token is valid for email: " + email);
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn(">>> Google Access Token validation failed: " + e.getMessage());
        }
        return false;
    }


//    @PostMapping("/introspect")
//    public ResponseEntity<Boolean> introspect(@RequestBody TokenIntrospectionRequest token) {
//
//        try {
//            if (token.getToken().startsWith("ya29.")) {
//                return ResponseEntity.ok(true);
//            }
//            Jwt jwt = securityUtil.checkValidJWTAccessToken(token.getToken());
//            String email = jwt.getSubject();
//            User user = userService.handleGetUserByUserName(email);
//            if (user != null && user.getStatus().equals(StatusEnum.ACTIVATED)) {
//                return ResponseEntity.ok(true);
//            }
//            return ResponseEntity.ok(false);
//        } catch (Exception e) {
//            return ResponseEntity.ok(false);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) throws Exception {
        // nap input username/password vao security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
        // Xac thuc nguoi dung = > can viet loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUserName(reqLoginDTO.getUsername());
        System.out.println("currentUserDB = " + currentUserDB.getId());
        if (currentUserDB != null && currentUserDB.getProvider().equals(AuthProvider.LOCAL)) {
            if (currentUserDB.getIsDeleted()) {
                throw new AccountDeletedException("User account has been deleted");
            }
            if (currentUserDB.getStatus().equals(StatusEnum.SUSPENDED)) {
                throw new AccountNotSuspendException("User account is suspended");
            }
            if (!currentUserDB.getStatus().equals(StatusEnum.ACTIVATED)) {
                throw new AccountNotActivatedException("User account is not activated");
            }

            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getName(),
                    currentUserDB.getEmail(),
                    currentUserDB.getStatus(),
                    currentUserDB.getProvider(),
                    currentUserDB.getRole()

            );
            res.setUser(user);
        }


        String access_token = securityUtil.createAccessToken(authentication.getName(), res);

        res.setAccessToken(access_token);
        // create refresh token
        String refresh_token = securityUtil.refreshToken(reqLoginDTO.getUsername(), res);
//        System.out.println("refresh_token = " + refresh_token);
        // update refresh token
        userService.updateUserToken(reqLoginDTO.getUsername(), refresh_token);
        res.setRefresh_token(refresh_token);
        // set cookie
        long expiresAt = System.currentTimeMillis() + accessTokenExpiration * 1000;
        long expiresAtInSec = expiresAt / 1000;
        res.setExpires_at(expiresAtInSec);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true).path("/")
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    //    // token set cookie
    @GetMapping("/account")
    @ApiMessage("Get account success")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUserName(authentication.getName());
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin();
            user.setId(currentUserDB.getId());
            user.setEmail(currentUserDB.getEmail());
            user.setUsername(currentUserDB.getName());
            userGetAccount.setUser(user);
            user.setRole(currentUserDB.getRole());
            user.setStatus(currentUserDB.getStatus());
            res.setUser(user);

        }
        return ResponseEntity.ok(userGetAccount);
    }

//    @PostMapping("/refresh")
//    @ApiMessage("Refresh token success")
//    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) throws Exception {
//        if (refreshToken == null) {
//            log.error(" Không nhận được refresh_token từ cookie!");
//            return ResponseEntity.badRequest().build();
//        }
//        log.info("Nhận được refresh_token: " + refreshToken);
//        Jwt decodeToken = this.securityUtil.checkVaildJWTREfreshToken(refreshToken);
//        String email = decodeToken.getSubject();
//        User currentUser = userService.getUserByRefreshToken(refreshToken, email);
//        if (currentUser == null) {
//            log.error("Refresh token is invalid");
//            throw new Exception("Refresh token is invalid");
//        } else if (!currentUser.getEmail().equals(email)) {
//            log.error("Refresh token is invalid");
//            throw new Exception("Refresh token is invalid");
//        } else {
//            log.info("Refresh token is valid");
//        }
//        ResLoginDTO res = new ResLoginDTO();
//        User currentUserDB = userService.handleGetUserByUserName(email);
//        if (currentUserDB != null) {
//            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin();
//            user.setId(currentUserDB.getId());
//            user.setEmail(currentUserDB.getEmail());
//            user.setUsername(currentUserDB.getName());
//            user.setRole(currentUserDB.getRole());
//            res.setUser(user);
//        }
//        String access_token = securityUtil.createAccessToken(email, res);
//
//        res.setAccessToken(access_token);
//        // create refresh token
//        String new_refresh_token = securityUtil.refreshToken(email, res);
//        // update refresh token
//        userService.updateUserToken(email, new_refresh_token);
//        // set cookie
//        long expiresAt = System.currentTimeMillis() + accessTokenExpiration * 1000;
//        long expiresAtInSec = expiresAt / 1000;
//        res.setExpires_at(expiresAtInSec);
//        res.setRefresh_token(new_refresh_token);
//        ResponseCookie cookie = ResponseCookie.from("refresh_token", new_refresh_token)
//                .httpOnly(true).path("/")
//                .secure(true)
//                .maxAge(refreshTokenExpiration)
//                .build();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(res);
//    }

    @PostMapping("/refresh")
    @ApiMessage("Refresh token success")
    public ResponseEntity<ResLoginDTO> refreshToken(@RequestBody Map<String, String> requestBody) throws Exception {
        String refreshToken = requestBody.get("refresh_token");
        if (refreshToken == null || refreshToken.isEmpty()) {
            log.error("Không nhận được refresh_token từ body!");
            return ResponseEntity.badRequest().build();
        }
        log.info("Nhận được refresh_token: " + refreshToken);

        Jwt decodeToken = this.securityUtil.checkVaildJWTREfreshToken(refreshToken);
        System.out.println("decodeToken = " + decodeToken);
        String email = decodeToken.getSubject();
        User currentUser = userService.getUserByRefreshToken(refreshToken, email);

        if (currentUser == null || !currentUser.getEmail().equals(email)) {
            log.error("Refresh token is invalid");
            throw new Exception("Refresh token is invalid");
        }
        log.info("Refresh token is valid");

        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUserName(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin();
            user.setId(currentUserDB.getId());
            user.setEmail(currentUserDB.getEmail());
            user.setUsername(currentUserDB.getName());
            user.setProvider(currentUserDB.getProvider());
            user.setRole(currentUserDB.getRole());
            res.setUser(user);
        }

        String access_token = securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

//        String new_refresh_token = securityUtil.refreshToken(email, res);
//
//        userService.updateUserToken(email, new_refresh_token);

        long expiresAt = System.currentTimeMillis() + accessTokenExpiration * 1000;
        res.setExpires_at(expiresAt / 1000);
        res.setRefresh_token(refreshToken);


        return ResponseEntity.ok(res);
    }


    @PostMapping("/logout")
    @ApiMessage("Logout success")
    public ResponseEntity<Void> logout() {
        Optional<String> currentUserEmail = SecurityUtil.getCurrentUserLogin();

        if (currentUserEmail.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or not authenticated");
        }

        String email = currentUserEmail.get();

        userService.updateUserToken(email, null);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody ReqUserCreateDto user) throws Exception {
        boolean isEmailExist = userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            log.error("Email is already exist");
            throw new Exception("Email is already exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userService.handleUser(userCreateMapper.toEntity(user));
        String activeToken = securityUtil.createActivationToken(newUser.getEmail());
        System.out.println("activeToken = " + activeToken);
        UserActiveEvent userActiveEvent = new UserActiveEvent(newUser.getName(), newUser.getEmail(), activeToken);
        userActiveEventKafkaTemplate.send(USER_ACTIVE_TOPIC, userActiveEvent);
        ResCreateUserDTO resCreateUserDTO = userMapper.toDto(newUser);
        log.info("User with id {} has been created", resCreateUserDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        HttpServletResponse response = null;
        try {
            String email = securityUtil.extractEmailFromToken(token);

            boolean isActivated = userService.activateUserByEmail(email);
            if (isActivated) {
                return ResponseEntity.ok("Tài khoản của bạn đã được kích hoạt thành công!");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng không tồn tại hoặc đã được kích hoạt trước đó.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không hợp lệ hoặc đã hết hạn.");
        }
    }

    @PostMapping("/resend-activation")
    @ApiMessage("Resend activation email")
    public ResponseEntity<ResResendActivationDto> resendActivationEmail(@RequestBody ReqEmailDto reqEmailDto) {
        ResResendActivationDto response = new ResResendActivationDto();
        response.setEmail(reqEmailDto.getEmail());
        User user = userService.handleGetUserByUserName(reqEmailDto.getEmail());
        if (user == null) {
            response.setMessage("Người dùng không tồn tại.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (user.getStatus().equals(StatusEnum.ACTIVATED)) {
            response.setMessage("Tài khoản của bạn đã được kích hoạt trước đó.");
            response.setStatus(StatusEnum.ACTIVATED);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String activeToken = securityUtil.createActivationToken(user.getEmail());
        System.out.println("activeToken = " + activeToken);
        UserActiveEvent userActiveEvent = new UserActiveEvent(user.getName(), user.getEmail(), activeToken);
        userActiveEventKafkaTemplate.send(USER_ACTIVE_TOPIC, userActiveEvent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-new-user-google")
    @ApiMessage("Create new user by google")
    public ResponseEntity<ResCreateUserDTO> createNewUserByGoogle(@RequestBody ReqUserGoogleDto reqUserGoogleDto) {
        User newUser = userService.saveUserByGoogle(reqUserGoogleDto);
        ResCreateUserDTO resCreateUserDTO = userMapper.toDto(newUser);
        log.info("User with id {} has been created", resCreateUserDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @PostMapping("/create-new-user-github")
    @ApiMessage("Create new user by github")
    public ResponseEntity<ResCreateUserDTO> createNewUserByGithub(@RequestBody ReqUserGoogleDto reqUserGoogleDto) {
        User newUser = userService.saveUserByGithub(reqUserGoogleDto);
        ResCreateUserDTO resCreateUserDTO = userMapper.toDto(newUser);
        log.info("User with id {} has been created", resCreateUserDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @PostMapping("/forgot-password")
    @ApiMessage("Forgot password")
    public ResponseEntity<String> forgotPassword(@RequestBody ReqEmailDto reqEmailDto) {
        User user = userService.handleGetUserByUserName(reqEmailDto.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        }
        if (user.getStatus().equals(StatusEnum.PENDING_ACTIVATION)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản của bạn chưa được kích hoạt.");
        }
        if (user.getStatus().equals(StatusEnum.DEACTIVATED)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản của bạn đã bị vô hiệu hóa.");
        }
        if (user.getStatus().equals(StatusEnum.SUSPENDED)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản của bạn đã bị tạm ngưng.");
        }
        String resetToken = userService.resetPassword(user);
        return ResponseEntity.ok(resetToken);

    }

    @GetMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token) throws Exception {
        Jwt jwt = securityUtil.checkValidJWTAccessToken(token);
        String email = jwt.getSubject();
        User user = userService.handleGetUserByUserName(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "http://localhost:3001/guest/auth/reset-password?id=" + user.getId())
                .build();
    }

    @PutMapping("/reset-password")
    @ApiMessage("Reset password")
    public ResponseEntity<Void> resetPassword(@RequestBody ReqResetPasswordDto reqResetPasswordDto) {
        userService.resetPasswordConfirm(reqResetPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-otp-cancel-account")
    @ApiMessage("Cancel account")
    public ResponseEntity<Void> sendOTPCanCelAccount(@RequestBody ReqCancelAccountDto reqCancelAccountDto) {
        userService.cancelAccount(reqCancelAccountDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel-account")
    @ApiMessage("Cancel account success")
    public ResponseEntity<Void> cancelAccount(@RequestBody ReqCancelDto reqCancelDto) {
        userService.cancelAccountOTP(reqCancelDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/suspend-account")
    @ApiMessage("Suspend account success")
    public ResponseEntity<Void> suspendAccount(@RequestBody ReqSuspendAccountDto reqSuspendAccountDto) {
        userService.suspendAccount(reqSuspendAccountDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-active-account-suspend")
    @ApiMessage("Send active account suspend success")
    public ResponseEntity<Void> sendActiveAccountSuspend(@RequestBody ReqActiveAccountSuspendDto reqActiveAccountSuspendDto) {
        userService.activeAccountSuspend(reqActiveAccountSuspendDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/active-account-suspend")
    @ApiMessage("Active account suspend success")
    public ResponseEntity<Void> activeAccountSuspend(@RequestBody ReqActiveAccountSuspendOTPDto reqActiveAccountSuspendDto) {
        userService.activeAccountSuspendOTP(reqActiveAccountSuspendDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-account-suspend")
    @ApiMessage("Check account suspend success")
    public ResponseEntity<?> checkAccountSuspend(@RequestBody ReqCheckAccountSuspendDto reqCheckAccountSuspendDto) {
        Object result = userService.checkAccountSuspend(reqCheckAccountSuspendDto);

        if (result instanceof User) {
            User user = (User) result;
            return ResponseEntity.ok(new ResLoginDTO.UserLogin(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getProvider(),
                    user.getRole()
            ));
        } else if (result instanceof List) {
            List<User> users = (List<User>) result;
            List<ResLoginDTO.UserLogin> userLogins = users.stream()
                    .map(user -> new ResLoginDTO.UserLogin(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getStatus(),
                            user.getProvider(),
                            user.getRole()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userLogins);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
