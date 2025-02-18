package net.javaguides.cart_service.repository;

import feign.Param;
import net.javaguides.cart_service.schema.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    @Query("{ 'cart_id': ?0, 'variant_id': ?1 }")
    List<CartItem> findByCartIdAndVariantId(String cartId, String variantId);

    List<CartItem> findByCartId(String cartId);

    List<CartItem> findCartItemByCartId(String cartId);

    List<CartItem> findCartItemByCartIdAndDeletedAtIsNull(String cartId);
}
