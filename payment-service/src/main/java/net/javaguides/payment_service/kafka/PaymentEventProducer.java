package net.javaguides.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * File: PaymentEventProducer.java
 * Author: Le Van Hoang
 * Date: 26/01/2025
 * Time: 10:00
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private static final String ORDER_TOPIC = "order-topic";
    private static final String NOTIFICATION_TOPIC = "notification-successful-payment-topic";

    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send(ORDER_TOPIC, paymentEvent);
        System.out.println("Payment event sent to topic: " + ORDER_TOPIC);
    }

    public void sendNotificationEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send(NOTIFICATION_TOPIC, paymentEvent);
        System.out.println("Notification event sent to topic: " + NOTIFICATION_TOPIC);
    }

}