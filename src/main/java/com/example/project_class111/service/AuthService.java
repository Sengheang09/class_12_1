package com.example.project_class111.service;

import com.example.project_class111.dto.RequestDto.LoginRequest;
import com.example.project_class111.dto.RequestDto.RegisterRequest;
import com.example.project_class111.dto.ResponseDto.LoginResponse;
import com.example.project_class111.dto.ResponseDto.MessageResponse;

public interface AuthService {

    MessageResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
