    package net.javaguides.order_service.services.impl;

    import lombok.RequiredArgsConstructor;
    import net.javaguides.order_service.mappers.IOrderMapper;
    import net.javaguides.order_service.repositories.IOrderRepository;
    import net.javaguides.order_service.services.IOrderService;
    import net.javaguides.order_service.services.httpClient.ICartServiceClient;
    import net.javaguides.order_service.shemas.Order;
    import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
    import net.javaguides.order_service.shemas.response.ResCartClientDto;
    import net.javaguides.order_service.shemas.response.ResCreateOrderDto;
    import net.javaguides.order_service.utils.constants.PaymentMethod;
    import net.javaguides.order_service.utils.constants.PaymentStatus;
    import org.springframework.stereotype.Service;

    /**
     * File: IOrderServiceImpl.java
     * Author: Le Van Hoang
     * Date: 25/01/2025
     * Time: 10:15
     * Version: 1.0
     * <p>
     * Copyright © 2025 Le Van Hoang. All rights reserved.
     */

    @Service
    @RequiredArgsConstructor
    public class IOrderServiceImpl implements IOrderService {
        private final IOrderRepository orderRepository;
        private final IOrderMapper orderMapper;
        private final ICartServiceClient cartServiceClient;

        @Override
        public ResCreateOrderDto createOrder(ReqCreateOrderDto reqCreateOrderDto) throws Exception {
            Order existingOrder = orderRepository.findByUserIdAndCartId(reqCreateOrderDto.getUserId(), reqCreateOrderDto.getCartId());

            if (existingOrder != null) {
                throw new Exception("Order already exists");
            }

            ResCartClientDto cart = cartServiceClient.getCartById(reqCreateOrderDto.getCartId());

            if (cart == null) {
                throw new Exception("Cart not found");
            }

            Order order = orderMapper.toOrder(reqCreateOrderDto);
            order.setPaymentId(null);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order.setTotalAmount(cart.getTotal());
            Order savedOrder = orderRepository.save(order);
            return orderMapper.toResCreateOrderDto(savedOrder);
        }
    }
