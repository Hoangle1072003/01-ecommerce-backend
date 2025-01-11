package net.javaguides.cart_service.service;

import net.javaguides.cart_service.schema.Cart;

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
    Cart save(Cart cart);
}
