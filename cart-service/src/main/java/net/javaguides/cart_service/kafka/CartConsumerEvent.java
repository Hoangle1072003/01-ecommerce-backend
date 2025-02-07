package net.javaguides.cart_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.service.ICartService;
import net.javaguides.event.dto.CartUpdateEvent;
import net.javaguides.event.dto.OrderCancelledEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * File: CartConsumerEvent.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 15:38
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
@RequiredArgsConstructor
public class CartConsumerEvent {
    private final ICartService cartService;

    @KafkaListener(topics = "CART_UPDATE_TOPIC")
    public void listenUpdateCart(CartUpdateEvent cartUpdateEvent) {
        System.out.println("Received cart update event: " + cartUpdateEvent);
        cartService.updateCartStatus(cartUpdateEvent.getCartId());
    }

    @KafkaListener(topics = "CART_UPDATE_STATUS_CANCELLED")
    public void listenUpdateCartStatusCancelled(OrderCancelledEvent orderCancelledEvent) {
        System.out.println("Received order cancelled event: " + orderCancelledEvent);
        cartService.updateCartStatusCancelled(orderCancelledEvent.getCartId());
    }
}
