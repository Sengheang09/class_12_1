package com.example.class_12_1.mapper;

import com.example.class_12_1.dto.Request.ProductRequest;
import com.example.class_12_1.dto.Response.ProductResponse;
import com.example.class_12_1.entity.Product;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        if (product == null) return null;
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .publicId(product.getPublicId())
                .stock(product.getStock())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }

    public static Product toEntity(ProductRequest request) {
        if (request == null) return null;
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice() != null ? request.getPrice() : 0.0)
                .stock(request.getStock() != null ? request.getStock() : 0)
                .build();
    }
}
