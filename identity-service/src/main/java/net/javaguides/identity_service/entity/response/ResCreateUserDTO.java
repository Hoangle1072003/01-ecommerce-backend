package net.javaguides.identity_service.entity.response;

/**
 * File: ResCreateUserDTO.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:26 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import net.javaguides.identity_service.utils.constant.GenderEnum;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link net.javaguides.identity_service.entity.User}
 */
@Value
public class ResCreateUserDTO implements Serializable {
    UUID id;
    String name;
    @NotBlank(message = "Email is required")
    String email;
    int age;
    String address;
    String phone;
    GenderEnum gender;
}
