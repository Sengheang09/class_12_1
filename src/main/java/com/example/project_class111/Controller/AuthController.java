package com.example.project_class111.Controller;

import com.example.project_class111.dto.RequestDto.LoginRequest;
import com.example.project_class111.dto.RequestDto.RegisterRequest;
import com.example.project_class111.dto.ResponseDto.LoginResponse;
import com.example.project_class111.dto.ResponseDto.MessageResponse;
import com.example.project_class111.service.AuthService;
import com.example.project_class111.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "Login and Registration APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping()
    @Operation(summary = "Register a new user")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(registerRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and obtain JWT Token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }
}
