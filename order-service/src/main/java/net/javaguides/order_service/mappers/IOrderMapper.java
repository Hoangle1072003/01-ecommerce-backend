package net.javaguides.order_service.mappers;

import net.javaguides.order_service.shemas.Order;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResOrderByIdDto;
import net.javaguides.order_service.shemas.response.ResPaymentMethod;
import org.mapstruct.Mapper;

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

}
