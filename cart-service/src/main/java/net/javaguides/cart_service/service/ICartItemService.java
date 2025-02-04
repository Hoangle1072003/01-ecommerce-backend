package net.javaguides.cart_service.service;

import net.javaguides.cart_service.schema.response.*;

import java.util.List;
import java.util.UUID;

/**
 * File: ICartItemService.java
 * Author: Le Van Hoang
 * Date: 21/01/2025
 * Time: 15:43
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface ICartItemService {
    ResCartItemDto getCartItemByUserId(UUID id) throws Exception;

    Void deleteCartItem(ResCartItemDeleteDto resCartItemDelete) throws Exception;

    List<ResGetCartItemDto> getCartItemByCartId(String id) throws Exception;

    ResUpdateCartItemDto updateCartItemQuantity(UUID userId, String productId, String variantId, int quantity) throws Exception;
}
