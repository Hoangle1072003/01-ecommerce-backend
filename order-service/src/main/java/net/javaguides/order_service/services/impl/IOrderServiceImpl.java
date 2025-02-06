package net.javaguides.order_service.services.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.CartUpdateEvent;
import net.javaguides.event.dto.PaymentEvent;
import net.javaguides.order_service.mappers.IOrderMapper;
import net.javaguides.order_service.repositories.IOrderRepository;
import net.javaguides.order_service.services.IOrderService;
import net.javaguides.order_service.services.httpClient.ICartServiceClient;
import net.javaguides.order_service.services.httpClient.IProductServiceClient;
import net.javaguides.order_service.shemas.Order;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.request.ReqUpdateOrderDto;
import net.javaguides.order_service.shemas.response.*;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.order_service.utils.constants.CartStatusEnum;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final IProductServiceClient productServiceClient;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final KafkaTemplate<String, CartItemClientEvent> kafkaTemplateStockUpdate;
    private final KafkaTemplate<String, CartUpdateEvent> kafkaTemplateCartUpdate;
    private static final String PAYMENT_TOPIC = "payment-topic";
    private static final String STOCK_UPDATE_TOPIC = "stock-update-topic";
    private static final String CART_UPDATE_TOPIC = "CART_UPDATE_TOPIC";

    @Override
    public ResCreateOrderDto createOrder(ReqCreateOrderDto reqCreateOrderDto) throws Exception {
        Order existingOrder = orderRepository.findByUserIdAndCartId(reqCreateOrderDto.getUserId(), reqCreateOrderDto.getCartId());

        if (existingOrder != null) {
            if (existingOrder.getPaymentStatus() == PaymentStatus.PENDING
                    || existingOrder.getPaymentStatus() == PaymentStatus.EXPIRED
            ) {
                existingOrder.setPaymentStatus(PaymentStatus.PENDING);
                existingOrder.setTotalAmount(cartServiceClient.getCartById(reqCreateOrderDto.getCartId()).getTotal());

                Order updatedOrder = orderRepository.save(existingOrder);

                PaymentEvent paymentEvent = new PaymentEvent(
                        updatedOrder.getId(),
                        reqCreateOrderDto.getUserId(),
                        reqCreateOrderDto.getPaymentMethod(),
                        PaymentStatus.PENDING,
                        updatedOrder.getTotalAmount()
                );
                kafkaTemplate.send(PAYMENT_TOPIC, paymentEvent);

                return orderMapper.toResCreateOrderDto(updatedOrder);
            } else {
                throw new Exception("Order already exists and is not cancelled");
            }
        }

        Cart cart = cartServiceClient.getCartById(reqCreateOrderDto.getCartId());
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


    @Override
    public ResOrderByIdDto getOrderById(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.map(orderMapper::toResOrderByIdDto).orElse(null);
    }

    @Override
    public List<ResPaymentMethod> getAllPaymentMethod() {
        List<Order> paymentMethods = orderRepository.findAll();
        return orderMapper.toResPaymentMethod(paymentMethods);
    }

    @Override
    public ResResultPaginationDTO getAllOrdersByUserId(String userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);

        List<ResOrderByUserIdDto> orderDtos = orders.getContent().stream().map(order -> {
            Cart cart = cartServiceClient.getCartById(order.getCartId());

            List<CartItemClientEvent> cartItems = cartServiceClient.getCartItemByCartId(cart.getId());


            cartItems.forEach(cartItem -> {
                ResProductVarientDto productVariant = productServiceClient.getProductVarient(cartItem.getVariantId());
                cartItem.setProductVariant(productVariant);
            });

            ResOrderByUserIdDto dto = orderMapper.toResOrderByUserIdDto(order, cart);
            dto.setCartItems(cartItems);

            return dto;
        }).collect(Collectors.toList());

        return new ResResultPaginationDTO(
                new ResMeta(
                        orders.getNumber() + 1,
                        orders.getSize(),
                        orders.getTotalPages(),
                        orders.getTotalElements()
                ),
                orderDtos
        );
    }

    @Override
    public ResOrderByUserIdDto getOrdersByOrderId(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Cart cart = cartServiceClient.getCartById(order.getCartId());
            List<CartItemClientEvent> cartItems = cartServiceClient.getCartItemByCartId(cart.getId());
            cartItems.forEach(cartItem -> {
                ResProductVarientDto productVariant = productServiceClient.getProductVarient(cartItem.getVariantId());
                cartItem.setProductVariant(productVariant);
            });
            ResOrderByUserIdDto dto = orderMapper.toResOrderByUserIdDto(order, cart);
            dto.setCartItems(cartItems);
            return dto;
        }
        return null;
    }

    @Override
    public ResOrderByIdDto getOrderByCartId(String id) throws Exception {
        Order order = orderRepository.findOrderByCartId(id);
        if (order != null) {
            if (order.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                List<CartItemClientEvent> cartItems = cartServiceClient.getCartItemByCartId(id);
                if (cartItems.isEmpty()) {
                    throw new Exception("Cart is empty");
                }
                return orderMapper.toResOrderByIdDto(order);
            } else {
                throw new Exception("Order already paid");
            }
        } else {
            throw new Exception("Order not found");
        }
    }

    @Override
    public ResOrderByIdDto updateOrder(ReqUpdateOrderDto reqUpdateOrderDto) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(reqUpdateOrderDto.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setTotalAmount(Double.parseDouble(reqUpdateOrderDto.getTotal_amount()));
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toResOrderByIdDto(updatedOrder);
        } else {
            throw new Exception("Order not found");
        }
    }

    @Override
    public ResOrderByIdDto updateOrderStatus(String id) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            if (orderOptional.get().getPaymentStatus() == PaymentStatus.PENDING) {
                Order order = orderOptional.get();
                order.setPaymentStatus(PaymentStatus.CANCELLED);
                Order updatedOrder = orderRepository.save(order);
                return orderMapper.toResOrderByIdDto(updatedOrder);
            } else {
                throw new Exception("Order already paid");
            }
        } else {
            throw new Exception("Order not found");
        }
    }


    @KafkaListener(topics = "order-topic")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        Optional<Order> orderOptional = orderRepository.findById(paymentEvent.getOrderId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Cart cart = cartServiceClient.getCartById(order.getCartId());

            if (paymentEvent.getPaymentStatus() == PaymentStatus.SUCCESS) {
                order.setPaymentStatus(PaymentStatus.SUCCESS);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " updated to SUCCESS");

                if (cart == null) {
                    System.out.println("Cart not found for cartId: " + order.getCartId());
                    return;
                }

                if (cart.getStatus() == CartStatusEnum.ACTIVE) {
                    CartUpdateEvent cartUpdateEvent = new CartUpdateEvent(cart.getId());
                    kafkaTemplateCartUpdate.send(CART_UPDATE_TOPIC, cartUpdateEvent);

                    List<CartItemClientEvent> cartItems = cartServiceClient.getCartItemByCartId(cart.getId());
                    if (cartItems != null && !cartItems.isEmpty()) {
                        for (CartItemClientEvent cartItem : cartItems) {
                            kafkaTemplateStockUpdate.send(STOCK_UPDATE_TOPIC, cartItem);
                            System.out.println("Stock update event sent for variantId: " + cartItem.getVariantId());
                        }
                    }
                }
            } else if (paymentEvent.getPaymentStatus() == PaymentStatus.FAILED) {
                order.setPaymentStatus(PaymentStatus.PENDING);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " updated to PENDING");
            }
        } else {
            System.out.println("Order not found for orderId: " + paymentEvent.getOrderId());
        }
    }
}
