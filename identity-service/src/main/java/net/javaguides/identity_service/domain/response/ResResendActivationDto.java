package net.javaguides.identity_service.domain.response;

import lombok.Data;
import net.javaguides.identity_service.utils.constant.StatusEnum;

/**
 * File: ResSresendActivationDto.java
 * Author: Le Van Hoang
 * Date: 09/02/2025
 * Time: 19:57
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ResResendActivationDto {
    private String message;
    private String email;
    private StatusEnum status;
}
