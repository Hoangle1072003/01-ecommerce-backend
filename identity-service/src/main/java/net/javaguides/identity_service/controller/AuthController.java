package net.javaguides.identity_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.event.dto.UserActiveEvent;
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
import net.javaguides.identity_service.utils.constant.StatusEnum;
import net.javaguides.identity_service.utils.error.AccountNotActivatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
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

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

    private final KafkaTemplate<String, UserActiveEvent> userActiveEventKafkaTemplate;
    private static final String USER_ACTIVE_TOPIC = "USER_ACTIVE_ACCOUNT";

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

        if (currentUserDB != null && currentUserDB.getStatus().equals(StatusEnum.ACTIVATED)) {
            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getName(),
                    currentUserDB.getEmail(),
                    currentUserDB.getStatus(),
                    currentUserDB.getRole()
            );
            res.setUser(user);
        } else {
            throw new AccountNotActivatedException("User is not activated");
        }

        String access_token = securityUtil.createAccessToken(authentication.getName(), res);

        res.setAccessToken(access_token);
        // create refresh token
        String refresh_token = securityUtil.refreshToken(reqLoginDTO.getUsername(), res);
        // update refresh token
        userService.updateUserToken(reqLoginDTO.getUsername(), refresh_token);
        // set cookie
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

    @GetMapping("/refresh")
    @ApiMessage("Refresh token success")
    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) throws Exception {
        Jwt decodeToken = this.securityUtil.checkVaildJWTREfreshToken(refreshToken);
        String email = decodeToken.getSubject();
        User currentUser = userService.getUserByRefreshToken(refreshToken, email);
        if (currentUser == null) {
            log.error("Refresh token is invalid");
            throw new Exception("Refresh token is invalid");
        } else if (!currentUser.getEmail().equals(email)) {
            log.error("Refresh token is invalid");
            throw new Exception("Refresh token is invalid");
        } else {
            log.info("Refresh token is valid");
        }
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUserName(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin();
            user.setId(currentUserDB.getId());
            user.setEmail(currentUserDB.getEmail());
            user.setUsername(currentUserDB.getName());
            user.setRole(currentUserDB.getRole());
            res.setUser(user);
        }
        String access_token = securityUtil.createAccessToken(email, res);

        res.setAccessToken(access_token);
        // create refresh token
        String new_refresh_token = securityUtil.refreshToken(email, res);
        // update refresh token
        userService.updateUserToken(email, new_refresh_token);
        // set cookie
        ResponseCookie cookie = ResponseCookie.from("refresh_token", new_refresh_token)
                .httpOnly(true).path("/")
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
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

}
