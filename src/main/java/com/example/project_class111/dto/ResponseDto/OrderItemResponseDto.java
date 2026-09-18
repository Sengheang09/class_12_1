package com.example.project_class111.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private Long id;
    private BigDecimal price;
    private int quantity;
    private Long productId;
    private String productName;

}
