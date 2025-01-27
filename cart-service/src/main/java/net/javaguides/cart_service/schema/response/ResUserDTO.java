package net.javaguides.cart_service.schema.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.GenderEnum;
import net.javaguides.cart_service.utils.constant.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * File: ResUserDTO.java
 * Author: Le Van Hoang
 * Date: 18/01/2025
 * Time: 16:54
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO  {
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

