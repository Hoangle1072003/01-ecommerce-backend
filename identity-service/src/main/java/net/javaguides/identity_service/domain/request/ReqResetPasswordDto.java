package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ReqResetPassword.java
 * Author: Le Van Hoang
 * Date: 12/02/2025
 * Time: 16:25
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqResetPasswordDto {
    private String id;
    private String password;
    private String confirmPassword;
}
