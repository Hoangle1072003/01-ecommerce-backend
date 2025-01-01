package net.javaguides.identity_service.entity.request;

/**
 * File: ReqUserCreateDto.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:30 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.javaguides.identity_service.entity.Role;
import net.javaguides.identity_service.utils.constant.GenderEnum;

import java.io.Serializable;

/**
 * DTO for {@link net.javaguides.identity_service.entity.User}
 */
@Data
public class ReqUserCreateDto implements Serializable {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;

    int age;
    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    String address;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number format: +xxx-xxxxxxxx")
    String phone;
    GenderEnum gender;
    private Role role;
}
