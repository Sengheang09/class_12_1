package com.example.class_12_1.mapper;

import com.example.class_12_1.dto.Request.UserRequest;
import com.example.class_12_1.dto.Response.UserResponse;
import com.example.class_12_1.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .build();
    }

    public static User toEntity(UserRequest request) {
        if (request == null) return null;
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }
}
