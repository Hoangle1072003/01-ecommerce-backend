package net.javaguides.payment_service.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.payment_service.configs.VNPAYConfig;
import net.javaguides.payment_service.repositories.IPaymentRepository;
import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.services.IPaymentService;
import net.javaguides.payment_service.utils.VNPayUtil;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * File: PaymentServiceImpl.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:34
 * Version: 1.0
 *
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class IPaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository paymentRepository;
    private final VNPAYConfig vnPayConfig;

    @Override
    public Payment processPaymentEvent(PaymentEvent paymentEvent) {
        Map<String, String> vnpParams = new HashMap<>(vnPayConfig.getVNPayConfig());
        vnpParams.put("vnp_Amount", String.valueOf((int)(paymentEvent.getTotalAmount() * 100)));
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
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public void processSuccessfulPayment(Map paymentDetails) {
        String orderId = (String) paymentDetails.get("vnp_TxnRef");
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        }
    }


}
