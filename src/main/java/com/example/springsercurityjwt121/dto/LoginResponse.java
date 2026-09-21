package com.example.springsercurityjwt121.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {

    private String username;

    private String token;

    private List<String> roles;
}
