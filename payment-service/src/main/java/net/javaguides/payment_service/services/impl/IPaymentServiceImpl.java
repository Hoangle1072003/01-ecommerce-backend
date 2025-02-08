package net.javaguides.payment_service.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.event.dto.PaymentUpdateStatusRefundEvent;
import net.javaguides.payment_service.configs.VNPAYConfig;
import net.javaguides.payment_service.kafka.PaymentEventProducer;
import net.javaguides.payment_service.mappers.IPaymentMapper;
import net.javaguides.payment_service.repositories.IPaymentRepository;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.schemas.request.RefundRequestDto;
import net.javaguides.payment_service.schemas.request.ReqPaymentDto;
import net.javaguides.payment_service.schemas.response.ResOrderByIdDto;
import net.javaguides.payment_service.schemas.response.ResPaymentDto;
import net.javaguides.payment_service.schemas.response.ResUserDTO;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.services.httpClient.IIdentityServiceClient;
import net.javaguides.payment_service.services.httpClient.IOrderServiceClient;
import net.javaguides.payment_service.utils.VNPayUtil;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * File: PaymentServiceImpl.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:34
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class IPaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository paymentRepository;
    private final VNPAYConfig vnPayConfig;
    private final PaymentEventProducer paymentEventProducer;
    private final IPaymentMapper paymentMapper;
    private final IIdentityServiceClient identityServiceClient;
    private final IOrderServiceClient orderServiceClient;
    private final KafkaTemplate<String, PaymentUpdateStatusRefundEvent> paymentUpdateStatusRefundEventKafkaTemplate;
    private static final String REFUND_TOPIC = "REFUND_TOPIC";

    @Override
    @Transactional
    public Payment processPaymentEvent(PaymentEvent paymentEvent) {
        String orderId = paymentEvent.getOrderId();

        Map<String, String> vnpParams = new HashMap<>(vnPayConfig.getVNPayConfig(orderId));
        vnpParams.put("vnp_Amount", String.valueOf((int) (paymentEvent.getTotalAmount() * 100)));
        vnpParams.put("vnp_IpAddr", "127.0.0.1");
        vnpParams.put("vnp_CreateDate", VNPayUtil.getCurrentTime());
        vnpParams.put("vnp_ExpireDate", VNPayUtil.getExpireTime());


        String queryString = VNPayUtil.getPaymentURL(vnpParams, true);
        String rawHashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), rawHashData);

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryString + "&vnp_SecureHash=" + secureHash;

        System.out.println("Generated Payment URL: " + paymentUrl);


        Payment existingPayment = paymentRepository.findByOrderId(orderId).orElse(null);

        if (existingPayment != null) {
            if (existingPayment.getPaymentStatus() == PaymentStatus.FAILED) {
                existingPayment.setPaymentUrl(paymentUrl);
                existingPayment.setTotalAmount(paymentEvent.getTotalAmount());
                existingPayment.setPaymentStatus(PaymentStatus.PENDING);
                existingPayment.setPaymentMethod(paymentEvent.getPaymentMethod());

                return paymentRepository.save(existingPayment);
            } else {
                return existingPayment;
            }
        }

        Payment newPayment = new Payment();
        newPayment.setPaymentUrl(paymentUrl);
        newPayment.setOrderId(orderId);
        newPayment.setPaymentStatus(PaymentStatus.PENDING);
        newPayment.setUserId(paymentEvent.getUserId());
        newPayment.setPaymentMethod(paymentEvent.getPaymentMethod());
        newPayment.setTotalAmount(paymentEvent.getTotalAmount());
        newPayment.setVnpTxnRef(vnpParams.get("vnp_TxnRef"));

        return paymentRepository.save(newPayment);
    }


    @Override
    @Transactional
    public void processSuccessfulPayment(Map<String, String> paymentDetails) {
        String orderId = paymentDetails.get("vnp_TxnRef");
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();

            if (PaymentStatus.PENDING.equals(payment.getPaymentStatus())) {
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                System.out.println("Payment updated to SUCCESS for orderId: " + orderId);
            } else {
                System.out.println("Payment already processed for orderId: " + orderId);
            }
        } else {
            System.out.println("Payment not found for orderId: " + orderId);
        }
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Map<String, String> paymentDetails) {
        String orderId = paymentDetails.get("vnp_TxnRef");
        String newStatus = paymentDetails.get("status");
        String transactionNo = paymentDetails.get("vnp_TransactionNo");
        String transactionDate = paymentDetails.get("vnp_TransactionDate");
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            PaymentStatus updatedStatus = PaymentStatus.valueOf(newStatus);

            if (!payment.getPaymentStatus().equals(updatedStatus) || payment.getTransactionNo() == null
                    || payment.getTransactionDate() == null
            ) {
                payment.setPaymentStatus(updatedStatus);
                payment.setTransactionNo(transactionNo);
                payment.setTransactionDate(transactionDate);
                paymentRepository.save(payment);
                System.out.println("Payment status updated to " + updatedStatus + " for orderId: " + orderId);

                PaymentEvent paymentEvent = new PaymentEvent(
                        orderId,
                        payment.getUserId(),
                        payment.getPaymentMethod(),
                        updatedStatus,
                        payment.getTotalAmount()
                );
                if (updatedStatus.equals(PaymentStatus.SUCCESS)) {
                    paymentEventProducer.sendPaymentEvent(paymentEvent);
//                    paymentEventProducer.sendNotificationEvent(paymentEvent);
                } else if (updatedStatus.equals(PaymentStatus.FAILED)) {
                    paymentEventProducer.sendPaymentEvent(paymentEvent);
                }
//                paymentEventProducer.sendNotificationEvent(paymentEvent);
            } else {
                System.out.println("Payment status already " + updatedStatus + " for orderId: " + orderId);
            }
        } else {
            System.out.println("Payment not found for orderId: " + orderId);
        }
    }

    @Override
    public ResPaymentDto findPayment(ReqPaymentDto reqPaymentDto) {
        try {
            ResUserDTO user = identityServiceClient.getUserById(reqPaymentDto.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found with ID: " + reqPaymentDto.getUserId());
            }

            List<Payment> payments = paymentRepository.findByUserId(reqPaymentDto.getUserId().toString());
            if (payments.isEmpty()) {
                throw new RuntimeException("No payment found for user with ID: " + reqPaymentDto.getUserId());
            }

            for (Payment payment : payments) {
                if (payment.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                    ResOrderByIdDto order = orderServiceClient.getOrder(reqPaymentDto.getOrderId());
                    if (order == null) {
                        throw new RuntimeException("Order not found with ID: " + reqPaymentDto.getOrderId());
                    }
                    if (order.getId().equals(payment.getOrderId())) {
                        if (order.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                            return paymentMapper.toResPaymentDto(payment);
                        } else {
                            throw new RuntimeException("Order with ID " + reqPaymentDto.getOrderId() + " has payment status: " + order.getPaymentStatus());
                        }
                    }
                }
            }
            throw new RuntimeException("No PENDING payment found for user with ID: " + reqPaymentDto.getUserId());
        } catch (Exception e) {
            System.out.println("Error occurred while finding payment: " + e.getMessage());
            throw new RuntimeException("Error occurred while finding payment: " + e.getMessage(), e);
        }
    }

    @Override
    public ResPaymentDto updatePaymentCancelStatus(String orderId) {
        try {
            Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);
            if (payment == null) {
                throw new RuntimeException("Payment not found with orderId: " + orderId);
            }
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);
            return paymentMapper.toResPaymentDto(payment);
        } catch (Exception e) {
            System.out.println("Error occurred while updating payment status: " + e.getMessage());
            throw new RuntimeException("Error occurred while updating payment status: " + e.getMessage(), e);
        }
    }

    @Override
    public Payment findByVnpTxnRef(String vnpTxnRef) {
        return paymentRepository.findByVnpTxnRef(vnpTxnRef);
    }

    @Override
    public void saveRefundTransaction(RefundRequestDto refundRequest, Map<String, String> responseBody) {
        try {
            Payment payment = paymentRepository.findByOrderId(refundRequest.getVnp_TxnRef())
                    .orElseThrow(() -> new RuntimeException("Payment not found with orderId: " + refundRequest.getVnp_TxnRef()));

            if (!PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) {
                throw new RuntimeException("Payment status is not SUCCESS for orderId: " + refundRequest.getVnp_TxnRef());
            }

            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);

            PaymentUpdateStatusRefundEvent paymentUpdateStatusRefundEvent = new PaymentUpdateStatusRefundEvent();
            paymentUpdateStatusRefundEvent.setOrderId(refundRequest.getVnp_TxnRef());
            paymentUpdateStatusRefundEventKafkaTemplate.send(REFUND_TOPIC, paymentUpdateStatusRefundEvent);
        } catch (Exception e) {
            System.out.println("Error occurred while saving refund transaction: " + e.getMessage());
            throw new RuntimeException("Error occurred while saving refund transaction: " + e.getMessage(), e);
        }
    }

//    @Override
//    public ResPaymentDto processRefund(RefundRequestDto refundRequest, HttpServletRequest request) {
//        try {
//            Payment payment = paymentRepository.findByOrderId(refundRequest.getVnp_TxnRef())
//                    .orElseThrow(() -> new RuntimeException("Payment not found with orderId: " + refundRequest.getVnp_TxnRef()));
//
//            if (!PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) {
//                throw new RuntimeException("Payment status is not SUCCESS for orderId: " + refundRequest.getVnp_TxnRef());
//            }
//
//            Map<String, String> refundParams = vnPayConfig.getVNPayConfig(payment.getOrderId());
//            refundParams.put("vnp_TransactionType", "02");
//            refundParams.put("vnp_Amount", String.valueOf((int) (refundRequest.getVnp_Amount() * 100)));
//            refundParams.put("vnp_TransactionNo", String.valueOf(payment.getTransactionNo()));
//            refundParams.put("vnp_CreateBy", "Admin");
//            refundParams.put("vnp_RequestId", VNPayUtil.getRandomNumber(10));
//
//            String queryUrl = VNPayUtil.getPaymentURL(refundParams, true);
//            String refundApiUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.postForEntity(refundApiUrl, null, String.class);
//
//            Map<String, String> responseMap = VNPayUtil.parseResponse(response.getBody());
//            if (!"00".equals(responseMap.get("vnp_ResponseCode"))) {
//                throw new RuntimeException("VNPAY Refund Failed: " + responseMap.get("vnp_Message"));
//            }
//
//            payment.setPaymentStatus(PaymentStatus.REFUNDED);
//            paymentRepository.save(payment);
//
//            return paymentMapper.toResPaymentDto(payment);
//
//        } catch (Exception e) {
//            System.out.println("Error occurred while processing refund: " + e.getMessage());
//            throw new RuntimeException("Error processing refund: " + e.getMessage(), e);
//        }
//    }

}
