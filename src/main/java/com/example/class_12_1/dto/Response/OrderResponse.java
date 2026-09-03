package com.example.class_12_1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private String status;

    private Long userId;

    private String username;

    private LocalDateTime updateAt;

    private List<OrderItemResponse> orderItems;
}
