package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ResResultPaginationDTO.java
 * Author: Le Van Hoang
 * Date: 05/02/2025
 * Time: 14:59
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResResultPaginationDTO {
    private ResMeta meta;
    private Object result;
}

