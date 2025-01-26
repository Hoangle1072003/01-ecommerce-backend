package net.javaguides.product_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.product_service.service.IProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * File: ProductConsumerEvent.java
 * Author: Le Van Hoang
 * Date: 22/01/2025
 * Time: 16:43
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
@RequiredArgsConstructor
public class ProductConsumerEvent {
    private final IProductService productService;

    @KafkaListener(topics = "stock-update-topic")
    public void listenStockUpdate(CartItemClientEvent cartItemClientEvent) {
        productService.updateProductStock(cartItemClientEvent.getProductId(), cartItemClientEvent.getVariantId(), cartItemClientEvent.getQuantity());
    }
}
