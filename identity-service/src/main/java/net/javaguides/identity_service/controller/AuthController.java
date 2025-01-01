package net.javaguides.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.entity.User;
import net.javaguides.identity_service.entity.request.ReqLoginDTO;
import net.javaguides.identity_service.entity.response.ResLoginDTO;
import net.javaguides.identity_service.mapper.IUserCreateMapper;
import net.javaguides.identity_service.mapper.IUserMapper;
import net.javaguides.identity_service.service.impl.UserServiceImpl;
import net.javaguides.identity_service.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final IUserMapper userMapper;
    private final IUserCreateMapper userCreateMapper;
    @Value("${spring.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        // nap input username/password vao security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
        // Xac thuc nguoi dung = > can viet loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.handleGetUserByUserName(reqLoginDTO.getUsername());

        if (currentUserDB != null) {
            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getName(),
                    currentUserDB.getEmail(),
                    currentUserDB.getStatus(),
                    currentUserDB.getRole()
            );
            res.setUser(user);
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
//    @GetMapping("/account")
//    @ApiMessage("Get account success")
//    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        ResLoginDTO res = new ResLoginDTO();
//        User currentUserDB = userService.handleGetUserByUserName(authentication.getName());
//        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
//        if (currentUserDB != null) {
//            ResLoginDTO.UserLogin user = new ResLoginDTO.UserLogin();
//            user.setId(currentUserDB.getId());
//            user.setEmail(currentUserDB.getEmail());
//            user.setUsername(currentUserDB.getName());
//            userGetAccount.setUser(user);
//            user.setRole(currentUserDB.getRole());
//            user.setStatus(currentUserDB.getStatus());
//            res.setUser(user);
//
//        }
//        return ResponseEntity.ok(userGetAccount);
//    }
//
//    @GetMapping("/refresh")
//    @ApiMessage("Refresh token success")
//    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) throws Exception {
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
//        ResponseCookie cookie = ResponseCookie.from("refresh_token", new_refresh_token)
//                .httpOnly(true).path("/")
//                .secure(true)
//                .maxAge(refreshTokenExpiration)
//                .build();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(res);
//    }
//
//    @PostMapping("/logout")
//    @ApiMessage("Logout success")
//    public ResponseEntity<Void> logout() {
//        Optional<String> currentUserEmail = SecurityUtil.getCurrentUserLogin();
//
//        if (currentUserEmail.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or not authenticated");
//        }
//
//        String email = currentUserEmail.get();
//
//        userService.updateUserToken(email, null);
//
//        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
//                .build();
//    }
//
//    @PostMapping("/register")
//    @ApiMessage("Register a new user")
//    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody ReqUserCreateDto user) throws Exception {
//        boolean isEmailExist = userService.isEmailExist(user.getEmail());
//        if (isEmailExist) {
//            log.error("Email is already exist");
//            throw new Exception("Email is already exist");
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User newUser = userService.handleUser(userCreateMapper.toEntity(user));
//        ResCreateUserDTO resCreateUserDTO = userMapper.toDto(newUser);
//        log.info("User with id {} has been created", resCreateUserDTO.getId());
//        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
//    }
}
