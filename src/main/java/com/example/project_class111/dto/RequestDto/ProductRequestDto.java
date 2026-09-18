package com.example.project_class111.dto.RequestDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductRequestDto {

    @NotBlank(message = "name must be not blank")
    @Size(min = 1, max = 50 , message = "name must be have character between 1 to 50.")
    private String name;

    @Size(min = 1, max = 1000)
    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0" , inclusive = false , message = "price must be greater than 0")
    private BigDecimal price;

    private MultipartFile file;

    private int stock;

    @NotNull(message = "category Id is required")
    private Long categoryId;

}
