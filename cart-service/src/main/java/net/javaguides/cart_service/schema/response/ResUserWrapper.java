package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ResUserWrapper.java
 * Author: Le Van Hoang
 * Date: 19/01/2025
 * Time: 22:08
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserWrapper {
    private int statusCode;
    private String error;
    private String message;
    private ResUserDTO data;
}
