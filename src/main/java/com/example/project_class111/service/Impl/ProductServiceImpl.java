package com.example.project_class111.service.Impl;

import com.example.project_class111.config.CloudinaryService;
import com.example.project_class111.dto.RequestDto.ProductRequestDto;
import com.example.project_class111.dto.ResponseDto.ProductResponseDto;
import com.example.project_class111.entity.Category;
import com.example.project_class111.entity.Product;
import com.example.project_class111.exception.BadRequestException;
import com.example.project_class111.exception.ResourceNotFoundException;
import com.example.project_class111.mapper.ProductMapper;
import com.example.project_class111.repo.CategoryRepository;
import com.example.project_class111.repo.ProductRepository;
import com.example.project_class111.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto){
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("No category found with id " + productRequestDto.getCategoryId()));

        String url = null;
        String publicId = null;

        if (productRequestDto.getFile() != null && !productRequestDto.getFile().isEmpty()) {
            Map image = cloudinaryService.upload(productRequestDto.getFile());

            url = (String) image.get("url");

            publicId = (String) image.get("public_id");
        }

        Product product = ProductMapper.toEntity(productRequestDto);
        product.setCategory(category);
        product.setImageUrl(url);
        product.setPublicId(publicId);

        Product saved = productRepository.save(product);

        return ProductMapper.toProductResponseDto(saved);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return ProductMapper.toProductResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("No category found with id " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("No category found with id " + productRequestDto.getCategoryId()));

        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
        product.setCategory(category);

        if (productRequestDto.getFile() != null && !productRequestDto.getFile().isEmpty()) {
            try {
                if (product.getPublicId() != null) {
                    cloudinaryService.delete(product.getPublicId());
                }
                Map image = cloudinaryService.upload(productRequestDto.getFile());
                String url = (String) image.get("secure_url");
                if (url == null) {
                    url = (String) image.get("url");
                }
                product.setImageUrl(url);
                product.setPublicId((String) image.get("public_id"));
            } catch (IOException e) {
                throw new BadRequestException("Failed to upload image to Cloudinary: " + e.getMessage());
            }
        }

        Product updated = productRepository.save(product);
        return ProductMapper.toProductResponseDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        if (product.getPublicId() != null) {
            try {
                cloudinaryService.delete(product.getPublicId());
            } catch (IOException e) {
                throw new BadRequestException("Failed to delete image from Cloudinary: " + e.getMessage());
            }
        }

        productRepository.delete(product);
    }
}
