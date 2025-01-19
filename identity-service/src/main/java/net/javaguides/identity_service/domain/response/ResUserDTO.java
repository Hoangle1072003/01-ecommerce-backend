package net.javaguides.identity_service.domain.response;

/**
 * File: ResUserDTO.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:27 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import net.javaguides.identity_service.utils.constant.GenderEnum;
import net.javaguides.identity_service.utils.constant.StatusEnum;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Value
public class ResUserDTO {
    UUID id;
    String name;
    @NotBlank(message = "Email is required")
    String email;
    int age;
    String address;
    String phone;
    GenderEnum gender;
    StatusEnum status;

    String createdBy;
    String updatedBy;
    Instant createdAt;
    Instant updatedAt;


    RoleUser role;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        UUID id;
        String name;
    }
}
