package net.javaguides.notification_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.event.dto.UserActiveEvent;
import net.javaguides.notification_service.dto.response.ResCartByIdDto;
import net.javaguides.notification_service.dto.response.ResCartItemByIdDto;
import net.javaguides.notification_service.dto.response.ResOrderByIdDto;
import net.javaguides.notification_service.dto.response.ResUserDTO;
import net.javaguides.notification_service.services.IEmailService;
import net.javaguides.notification_service.services.httpClient.ICartServiceClient;
import net.javaguides.notification_service.services.httpClient.IOrderServiceClient;
import net.javaguides.notification_service.services.httpClient.IUserServiceClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * File: NotificationEvent.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 20:58
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Service
@RequiredArgsConstructor
public class NotificationEvent {
    private final IUserServiceClient userServiceClient;
    private final IOrderServiceClient orderServiceClient;
    private final ICartServiceClient cartServiceClient;
    private final IEmailService emailService;

    @KafkaListener(topics = "notification-successful-payment-topic")
    public void handlePaymentSuccessEvent(PaymentEvent paymentEvent) {
        try {
            System.out.println("Received payment success event for Order ID: " + paymentEvent);
            String userId = paymentEvent.getUserId();
            String orderId = paymentEvent.getOrderId();
            System.out.println("Received payment success event for Order ID: " + orderId);
            System.out.println("Received payment success event for User ID: " + userId);
            ResUserDTO user = userServiceClient.getUserById(UUID.fromString(userId));
            ResOrderByIdDto order = orderServiceClient.getOrder(orderId);
            String userEmail = user.getEmail();
            String userName = user.getName();
            String cartId = order.getCartId();
            ResCartByIdDto cart = cartServiceClient.getCartById(cartId);
            System.out.println("Received payment success event for User : " + user);
            System.out.println("Received payment success event for Cart : " + cart);
            ResCartItemByIdDto cartItem = cartServiceClient.getCartItemByIdAndStatus(userId);
            System.out.println("Received payment success event for CartItem : " + cartItem);
            System.out.println("User email: " + userEmail);
            System.out.println("User name: " + userName);
            emailService.sendThankYouEmail(userName, userEmail);
            emailService.sendOrderConfirmationEmail(userName, userEmail, user, cart, order, cartItem);
        } catch (Exception e) {
            System.err.println("Error handling payment success event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "USER_ACTIVE_ACCOUNT")
    public void listenUserActiveEvent(UserActiveEvent userActiveEvent) {
        try {
            System.out.println("Received user active event: " + userActiveEvent);
            emailService.sendAccountActivationEmail(userActiveEvent);
        } catch (Exception e) {
            System.err.println("Error handling user active event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
