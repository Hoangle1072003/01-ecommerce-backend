package net.javaguides.cart_service.service;

import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.request.ReqCartDto;
import net.javaguides.cart_service.schema.request.ReqUpdateCart;
import net.javaguides.cart_service.schema.response.ResCartByUser;
import net.javaguides.cart_service.schema.response.ResCartUpdateDto;
import net.javaguides.cart_service.schema.response.ResProductVarientDto;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;

import java.util.UUID;

/**
 * File: ICartService.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:47
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface ICartService {
    Cart save(ReqCartDto reqCartDto) throws Exception;

    Cart findById(String id);

    Cart createNewCart(UUID userId);

    void processCartItem(Cart cart, ReqCartDto reqCartDto, String productId, ResProductVarientDto.VarientDto varient);

    ResCartByUser findByUserId(UUID userId);

    ResCartUpdateDto updateCartStatus(String cartId);

    ResCartUpdateDto updateCartStatusCancelled(String cartId);

    ResCartUpdateDto updateStatusCartCompleted(String cartId);

}
