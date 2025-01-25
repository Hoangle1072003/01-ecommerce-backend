package net.javaguides.payment_service.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.services.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * File: PaymentController.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:32
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    @KafkaListener(topics = "payment-topic")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        paymentService.processPaymentEvent(paymentEvent);
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<String> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        System.out.println("Payment status: " + status);
        if (status.equals("00")) {
            System.out.println("Payment success");
        } else {
            System.out.println("Payment failed");
        }
        return ResponseEntity.ok("Payment status: " + status);
    }
}
