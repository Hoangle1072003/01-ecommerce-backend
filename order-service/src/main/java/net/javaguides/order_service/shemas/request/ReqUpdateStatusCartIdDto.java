package net.javaguides.order_service.shemas.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ReqUpdateStatusCartIdDto.java
 * Author: Le Van Hoang
 * Date: 07/02/2025
 * Time: 21:39
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateStatusCartIdDto {
    private String cartId;
}
