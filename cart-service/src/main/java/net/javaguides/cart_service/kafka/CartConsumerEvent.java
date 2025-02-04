package net.javaguides.cart_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.service.ICartService;
import net.javaguides.event.dto.CartUpdateEvent;
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
}
