package net.javaguides.payment_service.schemas.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: RefundRequestDto.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 00:41
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequestDto {
    private String vnp_TxnRef;
    private Long vnp_Amount;
    private String vnp_OrderInfo;
    private String vnp_CreateBy;
}
