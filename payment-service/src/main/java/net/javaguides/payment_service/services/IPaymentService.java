package net.javaguides.payment_service.services;

import jakarta.servlet.http.HttpServletRequest;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.schemas.Payment;

import java.util.Map;

/**
 * File: PaymentService.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:33
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IPaymentService {
    Payment processPaymentEvent(PaymentEvent paymentEvent);
    void processSuccessfulPayment(Map<String, String> paymentDetails);
    void updatePaymentStatus(Map<String, String> paymentDetails);
}
