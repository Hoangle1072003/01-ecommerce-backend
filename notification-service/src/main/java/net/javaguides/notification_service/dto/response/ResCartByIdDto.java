package net.javaguides.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.notification_service.utils.constants.CartStatusEnum;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResCartByIdDto.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:58
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartByIdDto {
    private String id;
    private UUID userId;
    private CartStatusEnum status;
    private Instant modifiedOn;
    private Double total;
}
