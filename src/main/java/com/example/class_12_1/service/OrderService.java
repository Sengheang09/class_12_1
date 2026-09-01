package com.example.class_12_1.service;

import com.example.class_12_1.dto.Request.OrderRequest;
import com.example.class_12_1.dto.Response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(Long userId);
    OrderResponse updateOrderStatus(Long id, String status);

}
