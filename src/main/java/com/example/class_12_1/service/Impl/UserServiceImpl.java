package com.example.class_12_1.service.Impl;

import com.example.class_12_1.dto.Request.UserRequest;
import com.example.class_12_1.dto.Response.UserResponse;
import com.example.class_12_1.entity.User;
import com.example.class_12_1.exception.BadRequestException;
import com.example.class_12_1.mapper.UserMapper;
import com.example.class_12_1.repo.UserRepository;
import com.example.class_12_1.service.UserService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Server
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest request) {

        boolean exist = userRepository.existsByEmail(request.getEmail());
        if(exist){
            throw new BadRequestException("this email already exist.");
        }

        User user = UserMapper.toEntity(request);

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
