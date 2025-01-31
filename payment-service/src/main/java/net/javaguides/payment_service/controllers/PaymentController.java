package net.javaguides.payment_service.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.schemas.request.ReqPaymentDto;
import net.javaguides.payment_service.schemas.response.ResPaymentDto;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");

        System.out.println("Payment status: " + vnpResponseCode);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("vnp_TxnRef", vnpTxnRef);

        if ("00".equals(vnpResponseCode)) {
            System.out.println("Payment success");

            paymentDetails.put("status", PaymentStatus.SUCCESS.name());
            paymentService.updatePaymentStatus(paymentDetails);

            return ResponseEntity.ok("Payment success");
        } else if ("01".equals(vnpResponseCode)) {
            System.out.println("Payment failed");

            paymentDetails.put("status", PaymentStatus.FAILED.name());
            paymentService.updatePaymentStatus(paymentDetails);

            return ResponseEntity.ok("Payment failed");
        } else if ("02".equals(vnpResponseCode)) {
            System.out.println("Payment refunded");

            paymentDetails.put("status", PaymentStatus.REFUNDED.name());
            paymentService.updatePaymentStatus(paymentDetails);

            return ResponseEntity.ok("Payment refunded");
        } else {
            System.out.println("Unknown payment status");
            return ResponseEntity.badRequest().body("Unknown payment status");
        }
    }

    @GetMapping("/find-payment-by-user-id")
    public ResponseEntity<ResPaymentDto> findPaymentByUserId(@RequestBody ReqPaymentDto reqPaymentDto) {
        return ResponseEntity.ok(paymentService.findPayment(reqPaymentDto));
    }


}
