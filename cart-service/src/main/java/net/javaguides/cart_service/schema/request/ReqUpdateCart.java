package net.javaguides.cart_service.schema.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;

/**
 * File: ReqUpdateCart.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 15:42
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateCart {
    private String id;
}
