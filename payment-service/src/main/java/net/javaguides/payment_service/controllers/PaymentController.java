package net.javaguides.payment_service.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.schemas.request.RefundRequestDto;
import net.javaguides.payment_service.schemas.request.ReqPaymentDto;
import net.javaguides.payment_service.schemas.response.ResPaymentDto;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.utils.VNPayUtil;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_TransactionDate = request.getParameter("vnp_PayDate");

        System.out.println("Payment status: " + vnp_TransactionStatus);
        System.out.println("vnp_TxnRef: " + vnpTxnRef);
        System.out.println("vnp_TransactionNo: " + vnp_TransactionNo);
        System.out.println(" vnp_PayDate (vnp_TransactionDate): " + vnp_TransactionDate);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("vnp_TxnRef", vnpTxnRef);
        paymentDetails.put("vnp_TransactionNo", vnp_TransactionNo);
        paymentDetails.put("vnp_TransactionDate", vnp_TransactionDate);


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


    @PostMapping("/refund")
    public ResponseEntity<?> sendRefundRequest(@RequestBody RefundRequestDto refundRequest) {
        try {
            Payment originalPayment = paymentService.findByVnpTxnRef(refundRequest.getVnp_TxnRef());
            if (originalPayment == null) {
                return ResponseEntity.badRequest().body("Transaction not found");
            }

            if (refundRequest.getVnp_Amount() > originalPayment.getTotalAmount()) {
                return ResponseEntity.badRequest().body("Refund amount exceeds original amount");
            }

            String vnp_RequestId = UUID.randomUUID().toString().replace("-", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime transactionDateTime = LocalDateTime.parse(originalPayment.getTransactionDate(), formatter);
            ZonedDateTime transactionZonedDateTime = transactionDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            Instant transactionInstant = transactionZonedDateTime.toInstant();
            Date transactionDate = Date.from(transactionInstant);
            String vnp_TransactionDate = new SimpleDateFormat("yyyyMMddHHmmss").format(transactionDate);
            System.out.println("Fake vnp_TransactionDate: " + vnp_TransactionDate);


            Map<String, String> requestParams = new TreeMap<>();
            requestParams.put("vnp_RequestId", vnp_RequestId);
            requestParams.put("vnp_Version", "2.1.0");
            requestParams.put("vnp_Command", "refund");
            requestParams.put("vnp_TmnCode", "F90IELCW");
            requestParams.put("vnp_TransactionType", "02");
            requestParams.put("vnp_TxnRef", refundRequest.getVnp_TxnRef());
            requestParams.put("vnp_Amount", String.valueOf(refundRequest.getVnp_Amount() * 100));
            requestParams.put("vnp_OrderInfo", refundRequest.getVnp_OrderInfo());
            requestParams.put("vnp_TransactionDate", vnp_TransactionDate);
            requestParams.put("vnp_CreateBy", refundRequest.getVnp_CreateBy());
            requestParams.put("vnp_CreateDate", VNPayUtil.getCurrentTime());
            requestParams.put("vnp_IpAddr", "127.0.0.1");

            String data = String.join("|",
                    vnp_RequestId,
                    "2.1.0",
                    "refund",
                    "F90IELCW",
                    "02",
                    refundRequest.getVnp_TxnRef(),
                    String.valueOf(refundRequest.getVnp_Amount() * 100),
                    "", // vnp_TransactionNo
                    vnp_TransactionDate,
                    refundRequest.getVnp_CreateBy(),
                    requestParams.get("vnp_CreateDate"),
                    "127.0.0.1",
                    refundRequest.getVnp_OrderInfo()
            );

            String secureHash = VNPayUtil.hmacSHA512("KEK75W0MSYY3JTELC76V6OVRLXSYR5MO", data);
            requestParams.put("vnp_SecureHash", secureHash);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction",
                    requestEntity,
                    Map.class
            );

            Map<String, String> responseBody = response.getBody();
            if ("00".equals(responseBody.get("vnp_ResponseCode"))) {
                paymentService.saveRefundTransaction(refundRequest, responseBody);
                return ResponseEntity.ok(Collections.singletonMap("message", "Refund successful"));
            } else {
                return ResponseEntity.badRequest().body(responseBody);
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
