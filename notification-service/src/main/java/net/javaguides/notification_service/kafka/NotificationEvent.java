package net.javaguides.notification_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.*;
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
            emailService.sendThankYouEmailRegister(userActiveEvent.getName(), userActiveEvent.getEmail());
            emailService.sendAccountActivationEmail(userActiveEvent);
        } catch (Exception e) {
            System.err.println("Error handling user active event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "USER_FORGOT_PASSWORD_TOPIC")
    public void listenUserForgotPasswordEvent(UserForgotPasswordEvent userForgotPasswordEvent) {
        try {
            System.out.println("Received user forgot password event: " + userForgotPasswordEvent);
            emailService.sendForgotPasswordEmail(userForgotPasswordEvent.getEmail(), userForgotPasswordEvent.getToken());
        } catch (Exception e) {
            System.err.println("Error handling user forgot password event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "USER_CANCEL_ACCOUNT_TOPIC")
    public void listenUserCancelAccountEvent(UserCancelAccountEvent userCancelAccountEvent) {
        try {
            System.out.println("Received user cancel account event: " + userCancelAccountEvent);
            emailService.sendCancelAccountEmail(userCancelAccountEvent);
        } catch (Exception e) {
            System.err.println("Error handling user cancel account event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "USER_CANCEL_ACCOUNT_SUCCESS_TOPIC")
    public void listenUserCancelAccountSuccessEvent(UserCancelAccountSuccessEvent userCancelAccountSuccessEvent) {
        try {
            System.out.println("Received user cancel account success event: " + userCancelAccountSuccessEvent);
            emailService.sendCancelAccountSuccessEmail(userCancelAccountSuccessEvent);
        } catch (Exception e) {
            System.err.println("Error handling user cancel account success event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "USER_ACTIVE_SUSPEND_TOPIC")
    public void listenUserActiveSuspendEvent(UserActiveSuspendEvent userActiveSuspendEvent) {
        try {
            System.out.println("Received user active suspend event: " + userActiveSuspendEvent);
            emailService.sendActiveAccountSuccessEmail(userActiveSuspendEvent);
        } catch (Exception e) {
            System.err.println("Error handling user active suspend event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
