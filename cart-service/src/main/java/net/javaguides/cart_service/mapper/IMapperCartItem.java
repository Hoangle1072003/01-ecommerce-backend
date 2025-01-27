package net.javaguides.cart_service.mapper;

import net.javaguides.cart_service.schema.CartItem;
import net.javaguides.cart_service.schema.response.ResGetCartItemDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * File: IMapperCartItem.java
 * Author: Le Van Hoang
 * Date: 26/01/2025
 * Time: 14:55
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Mapper(componentModel = "spring")
public interface IMapperCartItem {
    ResGetCartItemDto toResGetCartItemDto(CartItem cartItem);
}
