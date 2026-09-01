package com.example.class_12_1.mapper;

import com.example.class_12_1.dto.Response.OrderItemResponse;
import com.example.class_12_1.entity.OrderItem;

public class OrderItemMapper {

    public static OrderItemResponse toResponse(OrderItem item) {
        if (item == null) return null;
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice() * item.getQuantity())
                .build();
    }
}
