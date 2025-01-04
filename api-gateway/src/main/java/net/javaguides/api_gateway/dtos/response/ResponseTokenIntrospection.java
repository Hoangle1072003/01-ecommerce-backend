package net.javaguides.api_gateway.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: TokenIntrospectionResponse.java
 * Author: Le Van Hoang
 * Date: 1/3/2025 (03/01/2025)
 * Time: 5:54 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTokenIntrospection {
    private int statusCode;
    private String error;
    private String message;
    private Boolean data;
}

