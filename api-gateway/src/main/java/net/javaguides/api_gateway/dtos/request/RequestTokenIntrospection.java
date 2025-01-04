package net.javaguides.api_gateway.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: TokenIntrospectionRequest.java
 * Author: Le Van Hoang
 * Date: 1/3/2025 (03/01/2025)
 * Time: 6:12 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTokenIntrospection {
    private String token;
}