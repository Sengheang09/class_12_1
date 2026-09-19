package com.example.project_class111.service;

import com.example.project_class111.dto.RequestDto.LoginRequest;
import com.example.project_class111.dto.RequestDto.RegisterRequest;
import com.example.project_class111.dto.RequestDto.UserRequestDto;
import com.example.project_class111.dto.ResponseDto.LoginResponse;
import com.example.project_class111.dto.ResponseDto.MessageResponse;
import com.example.project_class111.dto.ResponseDto.UserResponseDto;

import java.util.List;

public interface UserService {

    MessageResponse registerUser(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);
}
