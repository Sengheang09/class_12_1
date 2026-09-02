package com.example.class_12_1.controller;

import com.example.class_12_1.dto.Request.CategoryRequest;
import com.example.class_12_1.dto.Response.ApiResponse;
import com.example.class_12_1.dto.Response.CategoryResponse;
import com.example.class_12_1.entity.Category;
import com.example.class_12_1.service.CategoryService;
import com.example.class_12_1.service.Impl.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {

        return new ResponseEntity<>(
                    ApiResponse.success("Category have been created." , categoryService.createCategory(request)),
                    HttpStatus.CREATED
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Long id) {

        return new ResponseEntity<>(
                ApiResponse.success("found" , categoryService.getCategoryById(id)),
                HttpStatus.FOUND
        );

    }

}
