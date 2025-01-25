package net.javaguides.payment_service.schemas;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * File: AbstractMappedEntity.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 14:33
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
abstract public class AbstractMappedEntity {
    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
