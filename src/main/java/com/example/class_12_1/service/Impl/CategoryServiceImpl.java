package com.example.class_12_1.service.Impl;

import com.example.class_12_1.dto.Request.CategoryRequest;
import com.example.class_12_1.dto.Response.CategoryResponse;
import com.example.class_12_1.entity.Category;
import com.example.class_12_1.exception.BadRequestException;
import com.example.class_12_1.exception.ResourceNotFoundException;
import com.example.class_12_1.mapper.CategoryMapper;
import com.example.class_12_1.repo.CategoryRepository;
import com.example.class_12_1.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        boolean check = categoryRepository.existsByName(request.getName());
        if(check){
            return null;
        }

        Category category = categoryMapper.toEntity(request);

        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found with id: "+id)
        );

        return categoryMapper.toResponse(category);


    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return List.of();
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        return null;
    }

    @Override
    public void deleteCategory(Long id) {

    }
}
