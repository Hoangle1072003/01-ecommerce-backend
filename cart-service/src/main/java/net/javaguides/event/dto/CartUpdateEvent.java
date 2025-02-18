package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;

import java.io.Serializable;

/**
 * File: CartUpdateEvent.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 15:40
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateEvent implements Serializable {
    private static final long serialVersionUID = -3349753087535352957L;
    private String cartId;
}

