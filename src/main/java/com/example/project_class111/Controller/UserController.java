package com.example.project_class111.Controller;

import com.example.project_class111.dto.RequestDto.UserRequestDto;
import com.example.project_class111.dto.ResponseDto.ApiResponse;
import com.example.project_class111.dto.ResponseDto.UserResponseDto;
import com.example.project_class111.entity.User;
import com.example.project_class111.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User Management APIs")
@RestController
@RequestMapping({"/api/users", "/api/user"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(
                ApiResponse.success("User has been created successfully.", userService.createUser(requestDto)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto updated = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
