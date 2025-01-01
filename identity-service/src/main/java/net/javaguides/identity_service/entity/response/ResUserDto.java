package net.javaguides.identity_service.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * File: ResUserDto.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 2:08 PM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResUserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
}
