package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.CartStatusEnum;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResCartUpdateDto.java
 * Author: Le Van Hoang
 * Date: 07/02/2025
 * Time: 21:53
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