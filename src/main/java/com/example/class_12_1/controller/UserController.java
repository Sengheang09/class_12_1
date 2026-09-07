package com.example.class_12_1.controller;

import com.example.class_12_1.dto.Request.UserRequest;
import com.example.class_12_1.dto.Response.ApiResponse;
import com.example.class_12_1.dto.Response.UserResponse;
import com.example.class_12_1.service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request
    ){
        UserResponse userResponse = userService.createUser(request);

        return new ResponseEntity<>(
                ApiResponse.success("User created successfully",userResponse),
                HttpStatus.CREATED
        );
    }

}
