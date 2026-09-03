package com.example.class_12_1.service.Impl;

import com.cloudinary.Cloudinary;
import com.example.class_12_1.config.CloudinaryService;
import com.example.class_12_1.dto.Request.ProductRequest;
import com.example.class_12_1.dto.Response.ProductResponse;
import com.example.class_12_1.entity.Category;
import com.example.class_12_1.entity.Product;
import com.example.class_12_1.exception.ResourceNotFoundException;
import com.example.class_12_1.mapper.ProductMapper;
import com.example.class_12_1.repo.CategoryRepository;
import com.example.class_12_1.repo.ProductRepository;
import com.example.class_12_1.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository, CloudinaryService cloudinaryService)
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id "+request.getCategoryId())
        );

        Map image = cloudinaryService.uploadFile(request.getFile());

        String imageUrl = (String) image.get("url");
        String publicId = (String) image.get("public_id");

        Product product = ProductMapper.toEntity(request);

        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setPublicId(publicId);

        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts() {


//        return productRepository.findAllWithDetails().stream()
//                .map( product -> ProductMapper.toResponse(product)).toList();


        List<Product> productList = productRepository.findAllWithDetails();
        List<ProductResponse> allProductResponseDto = new ArrayList<>();
        for(Product product : productList){

            ProductResponse productResponseDto;
            productResponseDto = ProductMapper.toResponse(product);

            allProductResponseDto.add(productResponseDto);
        }

//        List<ProductResponse> productResponseList = productList.stream()
//                .map(product -> ProductMapper.toResponse(product)).toList();

        return allProductResponseDto;
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        return List.of();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
