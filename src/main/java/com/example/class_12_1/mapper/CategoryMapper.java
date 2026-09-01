package com.example.class_12_1.mapper;

import com.example.class_12_1.dto.Request.CategoryRequest;
import com.example.class_12_1.dto.Response.CategoryResponse;
import com.example.class_12_1.entity.Category;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createAt(category.getCreateAt())
                .updateAt(category.getUpdateAt())
                .build();
    }

    public static Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
