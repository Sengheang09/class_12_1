package com.example.class_12_1.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotNull(message = "name must not be null")
    @Size(min=2 , max = 100 , message = "Category name  must be between 2 to 100")
    private String name;

    @Size(min=10 , max = 1000 , message = "description must be between 10 to 1000")
    private String description;

}
