package com.example.springsercurityjwt121.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterRequest {

    @NotNull(message = "user name is required.")
    private String username;

    @NotNull(message = "email is required.")
    private String email;

    @NotNull(message = "password is required.")
    private String password;


}
