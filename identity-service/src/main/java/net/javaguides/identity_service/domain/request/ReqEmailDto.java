package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ReqEmailDto.java
 * Author: Le Van Hoang
 * Date: 09/02/2025
 * Time: 19:55
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEmailDto {
    private String email;
}
