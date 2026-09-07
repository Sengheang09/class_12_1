package com.example.class_12_1.service.Impl;

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
            CategoryRepository categoryRepository,
            CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId())
        );

        String imageUrl = null;
        String publicId = null;

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            Map image = cloudinaryService.uploadFile(request.getFile());
            imageUrl = (String) image.get("url");
            publicId = (String) image.get("public_id");
        }

        Product product = ProductMapper.toEntity(request);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setPublicId(publicId);

        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );
        return ProductMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAllWithDetails();
        List<ProductResponse> allProductResponseDto = new ArrayList<>();
        for (Product product : productList) {
            allProductResponseDto.add(ProductMapper.toResponse(product));
        }
        return allProductResponseDto;
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId())
        );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            if (product.getPublicId() != null) {
                cloudinaryService.deleteFile(product.getPublicId());
            }
            Map image = cloudinaryService.uploadFile(request.getFile());
            product.setImageUrl((String) image.get("url"));
            product.setPublicId((String) image.get("public_id"));
        }

        Product updated = productRepository.save(product);
        return ProductMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );

        if (product.getPublicId() != null) {
            cloudinaryService.deleteFile(product.getPublicId());
        }

        productRepository.delete(product);
    }
}
