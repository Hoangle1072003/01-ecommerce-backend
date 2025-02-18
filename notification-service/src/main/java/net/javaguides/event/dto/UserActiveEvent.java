package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: UserActiveEvent.java
 * Author: Le Van Hoang
 * Date: 09/02/2025
 * Time: 16:40
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveEvent {
    private String name;
    private String email;
    private String token;
}
