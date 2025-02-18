package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: UserForgotPasswordEvent.java
 * Author: Le Van Hoang
 * Date: 12/02/2025
 * Time: 14:53
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForgotPasswordEvent {
    private String email;
    private String token;
}
