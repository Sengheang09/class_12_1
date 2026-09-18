package com.example.project_class111.Controller;

import com.example.project_class111.dto.RequestDto.CategoryRequestDto;
import com.example.project_class111.dto.ResponseDto.ApiResponse;
import com.example.project_class111.dto.ResponseDto.CategoryResponseDto;
import com.example.project_class111.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Category", description = "Category Management APIs")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto created = categoryService.createCategory(requestDto);
        return new ResponseEntity<>(ApiResponse.success("Category created successfully", created), HttpStatus.CREATED);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", category));
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }

    @Operation(summary = "Update category by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto updated = categoryService.updateCategory(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updated));
    }

    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}
