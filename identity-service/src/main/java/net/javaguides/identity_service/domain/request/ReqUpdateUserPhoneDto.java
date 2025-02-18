package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * File: ReqUpdateUserPhoneDto.java
 * Author: Le Van Hoang
 * Date: 18/02/2025
 * Time: 13:41
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateUserPhoneDto {
    private UUID id;
    private String phone;
}
