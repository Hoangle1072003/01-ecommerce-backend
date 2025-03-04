package net.javaguides.identity_service.utils;

import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.response.ResLoginDTO;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * File: SecurityUtil.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 9:41 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${spring.security.authentication.jwt.base64-secret}")
    private String jwtSecret;
    @Value("${spring.security.authentication.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${spring.security.authentication.jwt.access-token-active-in-seconds}")
    private Long accessTokenActiveExpiration;

    @Value("${spring.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    private final JwtEncoder jwtEncoder;

    public String createAccessToken(String email, ResLoginDTO resLoginDTO) {

        ResLoginDTO.UserInsideToken userInsideToken = new ResLoginDTO.UserInsideToken();
        userInsideToken.setEmail(email);
        userInsideToken.setName(resLoginDTO.getUser().getUsername());
        userInsideToken.setEmail(resLoginDTO.getUser().getEmail());
        userInsideToken.setRole(resLoginDTO.getUser().getRole().getName());
        userInsideToken.setProvider(resLoginDTO.getUser().getProvider());


        Instant now = Instant.now();
        Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .claim("roles", Collections.singletonList(resLoginDTO.getUser().getRole().getName()))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    public String createActivationToken(String email) {
        Instant now = Instant.now();
        Instant validity = now.plus(accessTokenActiveExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("active", true)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    public String refreshToken (String email,ResLoginDTO resLoginDTO) {
        ResLoginDTO.UserInsideToken userInsideToken = new ResLoginDTO.UserInsideToken();
        userInsideToken.setEmail(email);
        userInsideToken.setName(resLoginDTO.getUser().getUsername());
        userInsideToken.setEmail(resLoginDTO.getUser().getEmail());
        userInsideToken.setRole(resLoginDTO.getUser().getRole().getName());
        userInsideToken.setProvider(resLoginDTO.getUser().getProvider());
        Instant now = Instant.now();
        Instant validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .claim("roles", Collections.singletonList(resLoginDTO.getUser().getRole().getName()))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }
    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }
    private static String extractPrincipal (Authentication authentication) {

        if (authentication == null) {

            return null;

        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) { return springSecurityUser.getUsername();

        } else if (authentication.getPrincipal() instanceof Jwt jwt) {

            return jwt.getSubject();

        } else if (authentication.getPrincipal() instanceof String s) {

            return s;

        }

        return null;
    }

    public Jwt checkValidJWTAccessToken(String token)throws Exception {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            log.info(">>> Access Token is valid");
            String email = jwtDecoder.decode(token).getSubject();
            log.info(">>> Email: " + email);
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            log.error(">>> Access Token Error: " + e.getMessage());
            System.out.println(">>> Access Token Error: " + e.getMessage());
            throw e;
        }
    }

    public Jwt checkVaildJWTREfreshToken(String token) {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            log.info(">>> Refresh Token is valid");
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            log.error(">>> Refresh Token Error: " + e.getMessage());
            System.out.println(">>> Refresh Token Error: " + e.getMessage());
            throw e;
        }
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtSecret).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    public String extractEmailFromToken(String token)throws Exception {
        Jwt jwt = checkValidJWTAccessToken(token);
        return jwt.getSubject();
    }

}
