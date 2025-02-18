package net.javaguides.payment_service.schemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.payment_service.utils.constant.GenderEnum;
import net.javaguides.payment_service.utils.constant.StatusEnum;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResUserDTO.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 22:32
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private UUID id;
    private String name;
    private String email;
    private int age;
    private String address;
    private String phone;
    private GenderEnum gender;
    private StatusEnum status;

    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleUser role;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private UUID id;
        private String name;
    }
}

