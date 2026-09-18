package com.example.project_class111.mapper;

import com.example.project_class111.dto.RequestDto.ProductRequestDto;
import com.example.project_class111.dto.ResponseDto.ProductResponseDto;
import com.example.project_class111.entity.Category;
import com.example.project_class111.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public static Product toEntity(ProductRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setStock(requestDto.getStock());

        return product;
    }

    public static ProductResponseDto toProductResponseDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(product.getId());
        responseDto.setName(product.getName());
        responseDto.setDescription(product.getDescription());
        responseDto.setPrice(product.getPrice());
        responseDto.setImageUrl(product.getImageUrl());
        responseDto.setStock(product.getStock());

        Category category = product.getCategory();
        if (category != null) {
            responseDto.setCategoryId(category.getId());
            responseDto.setCategoryName(category.getName());
        }
        return responseDto;
    }
}
