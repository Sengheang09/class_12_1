package com.example.class_12_1.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name must not be blank")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Stock must not be null")
    @Min(value = 1, message = "Stock must have at least 1")
    private Integer stock;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;

    private MultipartFile file;
}
