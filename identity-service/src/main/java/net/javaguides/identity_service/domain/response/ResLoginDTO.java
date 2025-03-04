package net.javaguides.identity_service.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import net.javaguides.identity_service.utils.constant.StatusEnum;

import java.util.UUID;

/**
 * File: ResLoginDTO.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:31 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ResLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refresh_token;
    private long expires_at;
    private UserLogin user;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin {
        private UUID id;
        private String username;
        private String email;
        private StatusEnum status;
        private AuthProvider provider;
        private Role role;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String email;
        private String name;
        private String role;
        private AuthProvider provider;
    }
}