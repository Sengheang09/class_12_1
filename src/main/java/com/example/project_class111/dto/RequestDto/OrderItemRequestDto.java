package com.example.project_class111.dto.RequestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "Product ID must not be null")
    private Long productId;

    @NotNull(message = "Product quantity must not be null")
    private Integer quantity;

}
