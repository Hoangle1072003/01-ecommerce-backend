package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ReqUserGoogleDto.java
 * Author: Le Van Hoang
 * Date: 11/02/2025
 * Time: 19:24
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUserGoogleDto {
    private String email;
    private String name;
    private String picture;
    private String sub;
}
