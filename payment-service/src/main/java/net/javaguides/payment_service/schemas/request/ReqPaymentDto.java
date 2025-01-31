package net.javaguides.payment_service.schemas.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * File: ReqPaymentDto.java
 * Author: Le Van Hoang
 * Date: 01/02/2025
 * Time: 00:17
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqPaymentDto {
    private String orderId;
    private UUID userId;
}
