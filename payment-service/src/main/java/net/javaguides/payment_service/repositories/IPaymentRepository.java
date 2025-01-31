package net.javaguides.payment_service.repositories;

import net.javaguides.payment_service.schemas.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File: IPaymentRepository.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 22:22
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Repository
public interface IPaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByUserId(String userId);
}
