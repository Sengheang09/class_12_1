package com.example.springsercurityjwt121.service;

import com.example.springsercurityjwt121.dto.LoginRequest;
import com.example.springsercurityjwt121.dto.LoginResponse;
import com.example.springsercurityjwt121.dto.MessageResponse;
import com.example.springsercurityjwt121.dto.RegisterRequest;

public interface UserService {
    MessageResponse registerUser(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
