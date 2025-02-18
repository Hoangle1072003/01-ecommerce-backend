package net.javaguides.payment_service.schemas.response;

import lombok.Data;
import net.javaguides.payment_service.utils.constant.PaymentMethod;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * File: ResPaymentDto.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 22:27
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
public class ResPaymentDto {
    private String id;
    private String orderId;
    private String userId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentUrl;
    private Double totalAmount;
}
