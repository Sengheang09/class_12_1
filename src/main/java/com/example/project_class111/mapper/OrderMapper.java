package com.example.project_class111.mapper;

import com.example.project_class111.dto.RequestDto.OrderRequestDto;
import com.example.project_class111.dto.ResponseDto.OrderItemResponseDto;
import com.example.project_class111.dto.ResponseDto.OrderResponseDto;
import com.example.project_class111.entity.Order;
import com.example.project_class111.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {



    public static OrderResponseDto toResponseDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setOrderData(order.getOrderData());
        responseDto.setTotalAmount(order.getTotalAmount());
        responseDto.setStatus(order.getStatus());

        User user = order.getUser();
        if (user != null) {
            responseDto.setUserId(user.getId());
            responseDto.setUserName(user.getName());
        }

        List<OrderItemResponseDto> itemResponses = order.getOrderItems() != null ?
                order.getOrderItems().stream()
                        .map(OrderItemMapper::toResponseDto)
                        .collect(Collectors.toList()) : Collections.emptyList();

        responseDto.setOrderItems(itemResponses);
        return responseDto;
    }
}
