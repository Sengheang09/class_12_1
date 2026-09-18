package com.example.project_class111.service.Impl;

import com.example.project_class111.dto.RequestDto.OrderItemRequestDto;
import com.example.project_class111.dto.RequestDto.OrderRequestDto;
import com.example.project_class111.dto.ResponseDto.OrderResponseDto;
import com.example.project_class111.entity.Order;
import com.example.project_class111.entity.OrderItem;
import com.example.project_class111.entity.Product;
import com.example.project_class111.entity.User;
import com.example.project_class111.exception.BadRequestException;
import com.example.project_class111.exception.ResourceNotFoundException;
import com.example.project_class111.mapper.OrderMapper;
import com.example.project_class111.repo.OrderItemRepository;
import com.example.project_class111.repo.OrderRepository;
import com.example.project_class111.repo.ProductRepository;
import com.example.project_class111.repo.UserRepository;
import com.example.project_class111.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderRequestDto.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderData(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItemsEntity = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : orderRequestDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product '" + product.getName()
                        + "'. Available: " + product.getStock() + ", requested: " + itemDto.getQuantity());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            orderItem.setTotalPricePerItem(total);

            orderItemsEntity.add(orderItem);
            totalAmount = totalAmount.add(total);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItemsEntity);

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return OrderMapper.toResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return orderRepository.findByUserId(userId).stream()
                .map(OrderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return OrderMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}
