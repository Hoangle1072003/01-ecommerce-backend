package net.javaguides.cart_service.mapper;

import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.response.ResCartByUser;
import org.mapstruct.Mapper;

/**
 * File: ICartMapper.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 15:44
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Mapper(componentModel = "spring")
public interface ICartMapper {
    ResCartByUser toResCartByUser(Cart cart);
}
