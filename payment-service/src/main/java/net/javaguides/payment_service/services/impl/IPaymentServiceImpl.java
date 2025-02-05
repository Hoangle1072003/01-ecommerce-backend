package net.javaguides.payment_service.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.configs.VNPAYConfig;
import net.javaguides.payment_service.kafka.PaymentEventProducer;
import net.javaguides.payment_service.mappers.IPaymentMapper;
import net.javaguides.payment_service.repositories.IPaymentRepository;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.schemas.request.ReqPaymentDto;
import net.javaguides.payment_service.schemas.response.ResOrderByIdDto;
import net.javaguides.payment_service.schemas.response.ResPaymentDto;
import net.javaguides.payment_service.schemas.response.ResUserDTO;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.services.httpClient.IIdentityServiceClient;
import net.javaguides.payment_service.services.httpClient.IOrderServiceClient;
import net.javaguides.payment_service.utils.VNPayUtil;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public Payment processPaymentEvent(PaymentEvent paymentEvent) {
        Map<String, String> vnpParams = new HashMap<>(vnPayConfig.getVNPayConfig());
        vnpParams.put("vnp_Amount", String.valueOf((int) (paymentEvent.getTotalAmount() * 100)));
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + paymentEvent.getOrderId());
        vnpParams.put("vnp_TxnRef", paymentEvent.getOrderId());
        vnpParams.put("vnp_IpAddr", "127.0.0.1");
        vnpParams.put("vnp_CreateDate", VNPayUtil.getCurrentTime());
        vnpParams.put("vnp_ExpireDate", VNPayUtil.getExpireTime());

        String queryString = VNPayUtil.getPaymentURL(vnpParams, true);
        String rawHashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), rawHashData);

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryString + "&vnp_SecureHash=" + secureHash;

        System.out.println("Generated Payment URL: " + paymentUrl);

        Payment payment = new Payment();
        payment.setPaymentUrl(paymentUrl);
        payment.setOrderId(paymentEvent.getOrderId());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setUserId(paymentEvent.getUserId());
        payment.setPaymentMethod(paymentEvent.getPaymentMethod());
        payment.setPaymentUrl(paymentUrl);
        payment.setTotalAmount(paymentEvent.getTotalAmount());
        paymentRepository.save(payment);
        return payment;
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
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            PaymentStatus updatedStatus = PaymentStatus.valueOf(newStatus);

            if (!payment.getPaymentStatus().equals(updatedStatus)) {
                payment.setPaymentStatus(updatedStatus);
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
                    paymentEventProducer.sendNotificationEvent(paymentEvent);
                } else if (updatedStatus.equals(PaymentStatus.FAILED)) {
                    paymentEventProducer.sendPaymentEvent(paymentEvent);
                } else if (updatedStatus.equals(PaymentStatus.PENDING)) {
                    paymentEventProducer.sendPaymentEvent(paymentEvent);
                } else if (updatedStatus.equals(PaymentStatus.EXPIRED)) {
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

}
