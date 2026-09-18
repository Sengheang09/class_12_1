package com.example.project_class111.service;

import com.example.project_class111.dto.RequestDto.ProductRequestDto;
import com.example.project_class111.dto.ResponseDto.ProductResponseDto;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto getProductById(Long id);

    List<ProductResponseDto> getAllProducts();

    List<ProductResponseDto> getProductsByCategoryId(Long categoryId);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

}
