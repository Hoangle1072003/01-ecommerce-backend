package net.javaguides.cart_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.repository.ICartRepository;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.service.ICartService;
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
    @Override
    public Cart save(Cart cart) {
        return null;
    }
}
