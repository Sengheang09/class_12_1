package com.example.project_class111.Controller;

import com.example.project_class111.dto.RequestDto.OrderRequestDto;
import com.example.project_class111.dto.ResponseDto.ApiResponse;
import com.example.project_class111.dto.ResponseDto.OrderResponseDto;
import com.example.project_class111.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Order", description = "Order Management APIs")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto created = orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>(ApiResponse.success("Order placed successfully", created), HttpStatus.CREATED);
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable Long id) {
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }

    @Operation(summary = "Get all orders")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    @Operation(summary = "Get orders by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully for user", orders));
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OrderResponseDto updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", updated));
    }

    @Operation(summary = "Delete order by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
    }
}
