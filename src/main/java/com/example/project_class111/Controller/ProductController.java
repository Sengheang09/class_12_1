package com.example.project_class111.Controller;

import com.example.project_class111.dto.RequestDto.ProductRequestDto;
import com.example.project_class111.dto.ResponseDto.ApiResponse;
import com.example.project_class111.dto.ResponseDto.ProductResponseDto;
import com.example.project_class111.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Product", description = "Product Management APIs")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product with optional image upload")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @ModelAttribute ProductRequestDto requestDto
    ){
        ProductResponseDto created = productService.createProduct(requestDto);
        return new ResponseEntity<>(ApiResponse.success("Product created successfully", created), HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @Operation(summary = "Get products by category ID")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByCategoryId(
            @PathVariable Long categoryId) {
        List<ProductResponseDto> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully for category", products));
    }

    @Operation(summary = "Update product by ID with optional new image")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequestDto requestDto) {
        ProductResponseDto updated = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updated));
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
