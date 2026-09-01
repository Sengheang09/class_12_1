package com.example.class_12_1.mapper;

import com.example.class_12_1.dto.Response.OrderItemResponse;
import com.example.class_12_1.dto.Response.OrderResponse;
import com.example.class_12_1.entity.Order;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        if (order == null) return null;

        List<OrderItemResponse> itemResponses = order.getOrderItems() != null ?
                order.getOrderItems().stream()
                        .map(OrderItemMapper::toResponse)
                        .collect(Collectors.toList()) : Collections.emptyList();

        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .username(order.getUser() != null ? order.getUser().getUsername() : null)
                .updateAt(order.getUpdateAt())
                .orderItems(itemResponses)
                .build();
    }
}
