package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResCartUpdateDto.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 15:44
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartUpdateDto {
    private String id;
    private UUID userId;
    private CartStatusEnum status;
    private Instant modifiedOn;
    private Double total;
}
