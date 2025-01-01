package net.javaguides.identity_service.utils;

import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.entity.response.ResLoginDTO;
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

    @Value("${spring.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    private final JwtEncoder jwtEncoder;

    public String createAccessToken(String email, ResLoginDTO resLoginDTO) {

        ResLoginDTO.UserInsideToken userInsideToken = new ResLoginDTO.UserInsideToken();
        userInsideToken.setEmail(email);
        userInsideToken.setName(resLoginDTO.getUser().getUsername());
        userInsideToken.setEmail(resLoginDTO.getUser().getEmail());
        Instant now = Instant.now();
        Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);

        List<String> listAuthorities = new ArrayList<>();
        listAuthorities.add("ROLE_USER_CREATE");
        listAuthorities.add("ROLE_ADMIN_UPDATE");

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .claim("permissions", listAuthorities)
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
        Instant now = Instant.now();
        Instant validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
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
}
