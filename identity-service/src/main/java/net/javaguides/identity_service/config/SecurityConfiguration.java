package net.javaguides.identity_service.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.controller.AuthController;
import net.javaguides.identity_service.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * File: SecurityConfiguration.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:01 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${spring.security.authentication.jwt.base64-secret}")
    private String jwtSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final OAuthenticationSuccessHandler oAuthenticationSuccessHandler;


    private final String googlePublicKeyUrl = "https://www.googleapis.com/oauth2/v3/certs";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        String[] whiteList = {
                "/",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh",
                "/api/v1/auth/register",
                "/api/v1/auth/activate",
                "/api/v1/auth/forgot-password",
                "/api/v1/auth/reset-password/**",
                "/api/v1/auth/resend-activation",
                "/api/v1/auth/send-active-account-suspend",
                "/api/v1/auth/active-account-suspend",
                "/api/v1/auth/create-new-user-google",
                "/api/v1/auth/create-new-user-github",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/oauth2/**",
                "/login/oauth2/code/**",
                "/login/**",
                "/auth/google/**",
//                "/api/v1/user/**",
                "/"

        };
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/roles/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/introspect").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuthenticationSuccessHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));


        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtSecret).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                SecurityUtil.JWT_ALGORITHM.getName());
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
//                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
//        return token -> {
//            try {
//                return jwtDecoder.decode(token);
//            } catch (Exception e) {
//                System.out.println(">>> JWT error: " + e.getMessage());
//                throw e;
//            }
//        };
//    }


    @Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder secretDecoder = jwtDecoderWithSecret();
        JwtDecoder googleDecoder = jwtDecoderWithGooglePublicKey();

        return token -> {
            System.out.println("Decoding token: " + token);
            if (token != null) {
                try {
                    return googleDecoder.decode(token);
                } catch (Exception e) {
                    System.out.println(">>> Error decoding Google token: " + e.getMessage());
                    return secretDecoder.decode(token);
                }
            }
            return null;
        };
    }


    private JwtDecoder jwtDecoderWithSecret() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM)
                .build();

        return token -> {
            try {

                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error (secret): " + e.getMessage());
                throw e;
            }
        };
    }

    private JwtDecoder jwtDecoderWithGooglePublicKey() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(googlePublicKeyUrl).build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error (Google): " + e.getMessage());
                throw e;
            }
        };
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            boolean isGoogle = validateGoogleIdToken(jwt.getTokenValue());
            if (isGoogle) {
                return extractGoogleAuthorities();
            } else {
                return jwtGrantedAuthoritiesConverter.convert(jwt);
            }
        });
        return jwtAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractGoogleAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    public boolean validateGoogleIdToken(String idToken) {
        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String email = (String) response.getBody().get("email");
                if (email != null) {
                    System.out.println(">>> Google ID Token is valid for email: " + email);
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(">>> Error validating Google ID Token: " + e.getMessage());
        }
        return false;
    }


}
