package com.example.class_12_1.service;

import com.example.class_12_1.dto.Request.UserRequest;
import com.example.class_12_1.dto.Response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
}
