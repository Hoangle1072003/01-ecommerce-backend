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
import org.springframework.transaction.annotation.Transactional;
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
        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");

        System.out.println("Payment status: " + vnp_TransactionStatus);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("vnp_TxnRef", vnpTxnRef);


        switch (vnp_TransactionStatus) {
            case "00":
                System.out.println("Payment success");
                paymentDetails.put("status", PaymentStatus.SUCCESS.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "02":
                System.out.println("Payment FAILED");
                paymentDetails.put("status", PaymentStatus.FAILED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;

            default:
                System.out.println("Unknown payment status");
                return ResponseEntity.badRequest().body("Unknown payment status");
        }

        return ResponseEntity.ok("Payment status updated: " + paymentDetails.get("status"));
    }


    @PostMapping("/find-payment-by-user-id")
    public ResponseEntity<ResPaymentDto> findPaymentByUserId(@RequestBody ReqPaymentDto reqPaymentDto) {
        return ResponseEntity.ok(paymentService.findPayment(reqPaymentDto));
    }
}
