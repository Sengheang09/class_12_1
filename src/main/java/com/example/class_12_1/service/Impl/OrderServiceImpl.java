package com.example.class_12_1.service.Impl;

import com.example.class_12_1.dto.Request.OrderItemRequest;
import com.example.class_12_1.dto.Request.OrderRequest;
import com.example.class_12_1.dto.Response.OrderItemResponse;
import com.example.class_12_1.dto.Response.OrderResponse;
import com.example.class_12_1.entity.Order;
import com.example.class_12_1.entity.OrderItem;
import com.example.class_12_1.entity.Product;
import com.example.class_12_1.entity.User;
import com.example.class_12_1.exception.BadRequestException;
import com.example.class_12_1.exception.ResourceNotFoundException;
import com.example.class_12_1.mapper.OrderItemMapper;
import com.example.class_12_1.mapper.OrderMapper;
import com.example.class_12_1.repo.OrderItemRepository;
import com.example.class_12_1.repo.OrderRepository;
import com.example.class_12_1.repo.ProductRepository;
import com.example.class_12_1.repo.UserRepository;
import com.example.class_12_1.service.OrderService;
import com.example.class_12_1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // check user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: "+request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0);

        double totalAmount = 0;

        List<OrderItemRequest> orderItems = request.getOrderItems();

        OrderItem orderItem = new OrderItem();

        List<OrderItem> orderItemEntityList = new ArrayList<>();

        for (OrderItemRequest storeOrderItem : orderItems){
            Product product = productRepository.findById(storeOrderItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find product with id: "+storeOrderItem.getProductId()));

            if((product.getStock() - storeOrderItem.getQuantity()) < 0){
                throw new BadRequestException("Product stock is not enough. ");
            }
            // new stock
            int newStock = product.getStock() - storeOrderItem.getQuantity();
            product.setStock(newStock);

            double subtotal = product.getPrice() * storeOrderItem.getQuantity();

            orderItem.setProduct(product);
            orderItem.setQuantity(storeOrderItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);

            orderItemEntityList.add(orderItem);

            totalAmount += subtotal;

        }
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItemEntityList);

        Order saved = orderRepository.save(order);

//        OrderResponse orderResponse = OrderMapper.toResponse(saved);
//
//        return orderResponse;
        return OrderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return List.of();
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return List.of();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {
        return null;
    }
}
