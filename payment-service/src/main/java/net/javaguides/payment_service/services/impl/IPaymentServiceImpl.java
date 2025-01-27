package net.javaguides.payment_service.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.configs.VNPAYConfig;
import net.javaguides.payment_service.kafka.PaymentEventProducer;
import net.javaguides.payment_service.repositories.IPaymentRepository;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.utils.VNPayUtil;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
                paymentEventProducer.sendPaymentEvent(paymentEvent);
                paymentEventProducer.sendNotificationEvent(paymentEvent);
            } else {
                System.out.println("Payment status already " + updatedStatus + " for orderId: " + orderId);
            }
        } else {
            System.out.println("Payment not found for orderId: " + orderId);
        }
    }
}
