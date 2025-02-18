package net.javaguides.order_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentUpdateStatusRefundEvent;
import net.javaguides.order_service.services.IOrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * File: OrderConsumer.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 15:11
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
@RequiredArgsConstructor
public class OrderConsumerEvent {
    private final IOrderService orderService;

    @KafkaListener(topics = "REFUND_TOPIC")
    public void listenRefund(PaymentUpdateStatusRefundEvent paymentUpdateStatusRefundEvent) throws Exception {
        System.out.println("Received refund event: " + paymentUpdateStatusRefundEvent.getOrderId());
        orderService.updateOrderStatusRefund(paymentUpdateStatusRefundEvent.getOrderId());
    }
}
