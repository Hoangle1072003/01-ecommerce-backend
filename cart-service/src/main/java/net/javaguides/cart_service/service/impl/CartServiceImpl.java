package net.javaguides.cart_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.repository.ICartItemRepository;
import net.javaguides.cart_service.repository.ICartRepository;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.CartItem;
import net.javaguides.cart_service.schema.request.ReqCartDto;
import net.javaguides.cart_service.schema.response.ResProductVarientDto;
import net.javaguides.cart_service.schema.response.ResUserDTO;
import net.javaguides.cart_service.service.ICartService;
import net.javaguides.cart_service.service.httpClient.IIdentityServiceClient;
import net.javaguides.cart_service.service.httpClient.IProductServiceClient;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import org.springframework.stereotype.Service;

/**
 * File: CartServiceImpl.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:48
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final ICartRepository cartRepository;
    private final IIdentityServiceClient identityServiceClient;
    private final IProductServiceClient productServiceClient;
    private final ICartItemRepository cartItemRepository;

    @Override
    public Cart save(ReqCartDto reqCartDto) {
        Cart existingCart = cartRepository.findByUserIdAndStatus(reqCartDto.getUserId(), CartStatusEnum.ACTIVE);
        Cart newCart = null;

        if (existingCart == null) {
            newCart = new Cart();
            newCart.setUserId(reqCartDto.getUserId());
            newCart.setStatus(CartStatusEnum.ACTIVE);
            newCart.setTotal(0.0);

            newCart = cartRepository.save(newCart);
        } else {
            newCart = existingCart;
        }

        ResUserDTO user = identityServiceClient.getUserById(reqCartDto.getUserId());
        System.out.println(user);
        if (user == null) {
            return null;
        }

        ResProductVarientDto productVarient = productServiceClient.getProductVarient(reqCartDto.getProductVariantId());
        System.out.println(productVarient);

        if (productVarient != null && productVarient.getVarients() != null && !productVarient.getVarients().isEmpty()) {
            ResProductVarientDto.VarientDto selectedVarient = productVarient.getVarients().get(0);

            CartItem cartItem = new CartItem();
            cartItem.setCartId(newCart.getId());
            cartItem.setProductId(productVarient.getId());
            cartItem.setVariantId(selectedVarient.getId());
            cartItem.setPrice(selectedVarient.getPrice());
            cartItem.setQuantity(reqCartDto.getQuantity());

            newCart.setTotal(newCart.getTotal() + selectedVarient.getPrice() * reqCartDto.getQuantity());

            cartItemRepository.save(cartItem);
        }

        return cartRepository.save(newCart);
    }



    @Override
    public Cart findById(String id) {
        return cartRepository.findById(id).orElse(null);
    }
}
