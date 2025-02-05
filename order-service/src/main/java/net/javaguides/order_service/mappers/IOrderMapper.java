package net.javaguides.order_service.mappers;

import net.javaguides.order_service.shemas.Order;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * File: IOrderMapper.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:13
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    ReqCreateOrderDto toReqCreateOrderDto(Order order);

    Order toOrder(ReqCreateOrderDto reqCreateOrderDto);

    ResCreateOrderDto toResCreateOrderDto(Order order);

    ResOrderByIdDto toResOrderByIdDto(Order order);

    List<ResPaymentMethod> toResPaymentMethod(List<Order> order);

    @Mapping(source = "order.userId", target = "userId")
    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "order.cartId", target = "cart.id")
    @Mapping(target = "cart", source = "cart")
    ResOrderByUserIdDto toResOrderByUserIdDto(Order order, Cart cart);
}
