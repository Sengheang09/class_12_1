package com.example.class_12_1.exception;

import com.example.class_12_1.dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
            BadRequestException ex
    ) {
        return new ResponseEntity<>(
                ApiResponse.error("Error: "+ex.getMessage()),
                HttpStatus.BAD_REQUEST
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {
        return new ResponseEntity<>(
                ApiResponse.error("Error: "+ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String , String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ){
        Map<String , String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String getField = ((FieldError) error).getField();
                    String getErrorCode = error.getCode();

                    errors.put(getField, getErrorCode);
                }
        );

        return new ResponseEntity<>(
                ApiResponse.<Map<String , String>>builder()
                        .success(false)
                        .message("Validation")
                        .data(errors)
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
