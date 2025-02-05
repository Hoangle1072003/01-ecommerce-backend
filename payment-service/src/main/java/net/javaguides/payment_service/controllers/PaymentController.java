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
        String vnpTransactionStatus = request.getParameter("vnp_TransactionStatus");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");

        System.out.println("Transaction Status: " + vnpTransactionStatus);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("vnp_TxnRef", vnpTxnRef);

        switch (vnpTransactionStatus) {
            case "00":
                System.out.println("Payment success");
                paymentDetails.put("status", PaymentStatus.SUCCESS.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "01":
                System.out.println("Payment not completed");
                paymentDetails.put("status", PaymentStatus.PENDING.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "02":
                System.out.println("Payment failed");
                paymentDetails.put("status", PaymentStatus.FAILED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "04":
                System.out.println("Payment pending (customer charged but not completed)");
                paymentDetails.put("status", PaymentStatus.PENDING.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "05":
                System.out.println("Payment processing");
                paymentDetails.put("status", PaymentStatus.PENDING.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "06":
                System.out.println("Refund request sent to bank");
                paymentDetails.put("status", PaymentStatus.REFUNDED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "07":
                System.out.println("Fraud detected");
                paymentDetails.put("status", PaymentStatus.FAILED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "08":
                System.out.println("Payment expired");
                paymentDetails.put("status", PaymentStatus.EXPIRED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "09":
                System.out.println("Refund request declined");
                paymentDetails.put("status", PaymentStatus.FAILED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "10":
                System.out.println("Payment completed (order shipped)");
                paymentDetails.put("status", PaymentStatus.SUCCESS.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "11":
                System.out.println("Payment canceled");
                paymentDetails.put("status", PaymentStatus.CANCELLED.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            case "20":
                System.out.println("Transaction settled for merchant");
                paymentDetails.put("status", PaymentStatus.SUCCESS.name());
                paymentService.updatePaymentStatus(paymentDetails);
                break;
            default:
                System.out.println("Unknown transaction status: " + vnpTransactionStatus);
                return ResponseEntity.badRequest().body("Unknown transaction status: " + vnpTransactionStatus);
        }

        return ResponseEntity.ok("Payment status updated: " + paymentDetails.get("status"));
    }


    @PostMapping("/find-payment-by-user-id")
    public ResponseEntity<ResPaymentDto> findPaymentByUserId(@RequestBody ReqPaymentDto reqPaymentDto) {
        return ResponseEntity.ok(paymentService.findPayment(reqPaymentDto));
    }
}
