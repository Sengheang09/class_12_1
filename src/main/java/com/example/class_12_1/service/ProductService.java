package com.example.class_12_1.service;

import com.example.class_12_1.dto.Request.ProductRequest;
import com.example.class_12_1.dto.Response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
