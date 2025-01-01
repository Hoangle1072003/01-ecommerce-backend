package net.javaguides.identity_service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


/**
 * File: ReqUserDto.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 10:47 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUserDto {
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
}
