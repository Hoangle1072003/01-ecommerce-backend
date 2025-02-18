package net.javaguides.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentUpdateCancelledEvent;
import net.javaguides.payment_service.services.IPaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * File: PaymentConsumer.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 00:06
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final IPaymentService paymentService;

    @KafkaListener(topics = "PAYMENT_UPDATE_STATUS_CANCELLED")
    public void listenUpdatePaymentStatusCancelled(PaymentUpdateCancelledEvent paymentUpdateCancelledEvent) {
        System.out.println("Received payment cancelled event: " + paymentUpdateCancelledEvent.getOrderId());
        paymentService.updatePaymentCancelStatus(paymentUpdateCancelledEvent.getOrderId());
    }
}
