package net.javaguides.identity_service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ResMeta.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:35 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMeta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}

