package net.javaguides.identity_service.repository;

import net.javaguides.identity_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:56 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

}
