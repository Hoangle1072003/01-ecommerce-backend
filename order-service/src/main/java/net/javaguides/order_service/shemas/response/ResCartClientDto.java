package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.CartStatusEnum;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResCartClientDto.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 17:05
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartClientDto {
    private String id;
    private UUID userId;
    private CartStatusEnum status;
    private Instant modifiedOn;
    private Double total;
}
