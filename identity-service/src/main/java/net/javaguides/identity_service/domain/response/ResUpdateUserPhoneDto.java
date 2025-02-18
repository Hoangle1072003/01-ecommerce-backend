package net.javaguides.identity_service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.utils.constant.GenderEnum;
import net.javaguides.identity_service.utils.constant.StatusEnum;

import java.util.UUID;

/**
 * File: ResUpdateUserPhoneDto.java
 * Author: Le Van Hoang
 * Date: 18/02/2025
 * Time: 13:43
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserPhoneDto {
    private UUID id;
    private String phone;
}

