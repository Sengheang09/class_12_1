package com.example.springsercurityjwt121.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {

    @NotNull(message = "user name is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

}
