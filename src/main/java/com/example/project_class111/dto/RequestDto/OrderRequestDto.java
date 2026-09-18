package com.example.project_class111.dto.RequestDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequestDto> orderItems;
}
