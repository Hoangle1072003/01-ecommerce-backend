package net.javaguides.order_service.services.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.order_service.mappers.IOrderMapper;
import net.javaguides.order_service.repositories.IOrderRepository;
import net.javaguides.order_service.services.IOrderService;
import net.javaguides.order_service.services.httpClient.ICartServiceClient;
import net.javaguides.order_service.shemas.Order;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResCartClientDto;
import net.javaguides.order_service.shemas.response.ResCreateOrderDto;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private static final String PAYMENT_TOPIC = "payment-topic";

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

        PaymentEvent paymentEvent = new PaymentEvent(
                savedOrder.getId(),
                reqCreateOrderDto.getUserId(),
                reqCreateOrderDto.getPaymentMethod(),
                PaymentStatus.PENDING,
                cart.getTotal()
        );

        kafkaTemplate.send(PAYMENT_TOPIC, paymentEvent);
        return orderMapper.toResCreateOrderDto(savedOrder);
    }

    @KafkaListener(topics = "order-topic")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        Optional<Order> orderOptional = orderRepository.findById(paymentEvent.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            if (paymentEvent.getPaymentStatus() == PaymentStatus.SUCCESS) {
                order.setPaymentStatus(PaymentStatus.SUCCESS);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " updated to SUCCESS");
            } else if (paymentEvent.getPaymentStatus() == PaymentStatus.FAILED) {
                order.setPaymentStatus(PaymentStatus.FAILED);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " updated to FAILED");
            }
        } else {
            System.out.println("Order not found for orderId: " + paymentEvent.getOrderId());
        }
    }
}
