package net.javaguides.order_service.repositories;

import net.javaguides.order_service.shemas.Order;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * File: IOrderRepository.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:15
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Repository
public interface IOrderRepository extends MongoRepository<Order, String> {
    Order findByUserIdAndCartId(String userId, String cartId);
}
