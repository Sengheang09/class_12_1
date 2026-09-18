package com.example.project_class111.exception;

import com.example.project_class111.dto.ResponseDto.ApiResponse;
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex
    ){
        ApiResponse<Object> apiResponse = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(apiResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String , String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ){
        Map<String , String>  errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();

                    errors.put(fieldName, errorMessage);
                }
        );

        return new ResponseEntity<>(
                ApiResponse.<Map<String,String>>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .timestamp(LocalDateTime.now())
                        .build()
                , HttpStatus.BAD_REQUEST
        );
    }

}

