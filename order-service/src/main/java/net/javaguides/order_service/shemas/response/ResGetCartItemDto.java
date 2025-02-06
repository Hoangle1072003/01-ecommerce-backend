package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ResGetCartItemDto.java
 * Author: Le Van Hoang
 * Date: 07/02/2025
 * Time: 01:09
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResGetCartItemDto {
    private String id;
    private String cartId;
    private String productId;
    private String variantId;
    private int quantity;
    private Double price;
}
