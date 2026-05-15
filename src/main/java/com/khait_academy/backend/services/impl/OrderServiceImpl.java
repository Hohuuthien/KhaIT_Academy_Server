package com.khait_academy.backend.services.impl;
import com.khait_academy.backend.dto.request.OrderRequest;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.entities.OrderItem;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.OrderMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.OrderRepository;
import com.khait_academy.backend.repositories.UserRepository;
import com.khait_academy.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(Long userId, OrderRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .paymentMethod(request.getPaymentMethod())
                .build();

        for (Long courseId : request.getCourseIds()) {

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

            OrderItem item = OrderItem.fromCourse(course);

            order.addItem(item);
        }

        order.recalculateTotal();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }
@Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toResponse(order);
    }

    @Override
    public void cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.cancel();

        orderRepository.save(order);
    }

    @Override
    public void markPaid(Long id, String transactionId) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.markPaid(transactionId);

        orderRepository.save(order);
    }
}