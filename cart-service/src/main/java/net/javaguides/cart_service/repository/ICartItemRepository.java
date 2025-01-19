package net.javaguides.cart_service.repository;

import net.javaguides.cart_service.schema.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * File: ICartItemRepository.java
 * Author: Le Van Hoang
 * Date: 18/01/2025
 * Time: 23:04
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Repository
public interface ICartItemRepository extends MongoRepository<CartItem, String> {
}
