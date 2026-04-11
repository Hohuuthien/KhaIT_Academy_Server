package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.OrderRequest;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.enums.OrderStatus;
import com.khait_academy.backend.mapper.OrderMapper;
import com.khait_academy.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public OrderResponse createOrder(String email, OrderRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Course> courses = courseRepository.findAllById(request.getCourseIds());

        double total = courses.stream()
                .mapToDouble(Course::getPrice)
                .sum();

        Order order = Order.builder()
                .user(user)
                .totalPrice(total)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = courses.stream().map(course ->
                OrderItem.builder()
                        .order(order)
                        .course(course)
                        .price(course.getPrice())
                        .build()
        ).toList();

        order.setItems(items);

        return OrderMapper.toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getMyOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}