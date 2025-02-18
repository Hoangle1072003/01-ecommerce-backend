package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.shemas.response.ResProductVarientDto;

/**
 * File: CartItemClientEvent.java
 * Author: Le Van Hoang
 * Date: 26/01/2025
 * Time: 14:51
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemClientEvent {
    private String id;
    private String cartId;
    private String productId;
    private String variantId;
    private int quantity;
    private Double price;
    private ResProductVarientDto productVariant;
}
