package net.javaguides.notification_service.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.notification_service.utils.constants.GenderEnum;
import net.javaguides.notification_service.utils.constants.StatusEnum;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResUserDTO.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:32
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    UUID id;
    String name;
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
