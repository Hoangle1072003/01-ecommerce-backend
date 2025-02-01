package net.javaguides.cart_service.repository;

import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * File: ICartRepository.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:46
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Repository
public interface ICartRepository extends MongoRepository<Cart, String> {
    Cart findByUserIdAndStatus(UUID userId, CartStatusEnum status);

    List<Cart> findCartByUserId(UUID userId);
}
