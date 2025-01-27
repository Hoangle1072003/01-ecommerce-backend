package net.javaguides.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.notification_service.utils.constants.PaymentMethod;
import net.javaguides.notification_service.utils.constants.PaymentStatus;

/**
 * File: ResOrderByIdDto.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:52
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderByIdDto {
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double totalAmount;
}
