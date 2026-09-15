package com.example.springsercurityjwt121.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {

    private String username;

    private String token;

    private String expiration;
}
