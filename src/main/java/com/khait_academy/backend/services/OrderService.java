// package com.khait_academy.backend.services;

// import com.khait_academy.backend.dto.request.OrderRequest;
// import com.khait_academy.backend.dto.response.OrderResponse;
// import com.khait_academy.backend.entities.*;
// import com.khait_academy.backend.enums.*;
// import com.khait_academy.backend.exception.BadRequestException;
// import com.khait_academy.backend.exception.ResourceNotFoundException;
// import com.khait_academy.backend.mapper.OrderMapper;
// import com.khait_academy.backend.repositories.*;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// @Transactional(readOnly = true)
// public class OrderService {

//     private final OrderRepository orderRepository;
//     private final UserRepository userRepository;
//     private final CourseRepository courseRepository;
//     private final EnrollmentRepository enrollmentRepository;

//     // ================= PRIVATE =================

//     private User getUser(Long userId) {
//         return userRepository.findById(userId)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("User", "id", userId));
//     }

//     private List<Course> getCourses(List<Long> courseIds) {
//         List<Course> courses = courseRepository.findAllById(courseIds);

//         if (courses.size() != courseIds.size()) {
//             throw new BadRequestException("Một số khóa học không tồn tại");
//         }

//         return courses;
//     }

//     private void validateRequest(OrderRequest request) {
//         if (request == null || request.getCourseIds() == null || request.getCourseIds().isEmpty()) {
//             throw new BadRequestException("Danh sách courseIds không được rỗng");
//         }
//     }

//     private void validateCourseAvailable(Long userId, List<Course> courses) {
//         for (Course course : courses) {

//             if (!Boolean.TRUE.equals(course.getIsPublished())) {
//                 throw new BadRequestException("Khóa học chưa publish: " + course.getTitle());
//             }

//             if (enrollmentRepository.existsByUser_IdAndCourse_Id(userId, course.getId())) {
//                 throw new BadRequestException("Bạn đã sở hữu khóa học: " + course.getTitle());
//             }
//         }
//     }

//     // ================= DISCOUNT =================

//     private BigDecimal calculatePrice(Course course) {

//         BigDecimal price = course.getPrice() != null
//                 ? course.getPrice()
//                 : BigDecimal.ZERO;

//         return price.setScale(2, RoundingMode.HALF_UP);
//     }

//     private BigDecimal calculateTotal(List<Course> courses) {
//         return courses.stream()
//                 .map(this::calculatePrice)
//                 .reduce(BigDecimal.ZERO, BigDecimal::add)
//                 .setScale(2, RoundingMode.HALF_UP);
//     }

//     // ================= CREATE ORDER =================

//     @Transactional
//     public OrderResponse createOrder(Long userId, OrderRequest request) {

//         validateRequest(request);

//         User user = getUser(userId);

//         List<Long> ids = request.getCourseIds().stream().distinct().toList();
//         List<Course> courses = getCourses(ids);

//         validateCourseAvailable(userId, courses);

//         BigDecimal total = calculateTotal(courses);

//         Order order = Order.builder()
//                 .user(user)
//                 .totalPrice(total) 
//                 .status(OrderStatus.PENDING)
//                 .paymentMethod(request.getPaymentMethod())
//                 .createdAt(LocalDateTime.now())
//                 .build();

//         List<OrderItem> items = courses.stream()
//                 .map(course ->
//                         OrderItem.builder()
//                                 .order(order)
//                                 .course(course)
//                                 .price(course.getPrice()) // ✅ FIX
//                                 .build()
//                 )
//                 .toList();

//         order.setItems(items);

//         Order saved = orderRepository.save(order);

//         log.info("Create order: userId={}, total={}, items={}",
//                 userId, total, items.size());

//         return OrderMapper.toResponse(saved);
//     }

//     // ================= MY ORDERS =================

//     public Page<OrderResponse> getMyOrders(Long userId, Pageable pageable) {

//         return orderRepository
//                 .findByUser_IdOrderByCreatedAtDesc(userId, pageable)
//                 .map(OrderMapper::toResponse);
//     }

//     // ================= PAYMENT SUCCESS =================

//     @Transactional
//     public void markAsPaid(Long orderId) {

//         Order order = orderRepository.findWithDetailsById(orderId)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("Order", "id", orderId));

//         if (order.getStatus() == OrderStatus.PAID) return;

//         order.setStatus(OrderStatus.PAID);

//         for (OrderItem item : order.getItems()) {

//             boolean exists = enrollmentRepository.existsByUser_IdAndCourse_Id(
//                     order.getUser().getId(),
//                     item.getCourse().getId()
//             );

//             if (exists) continue; // ✅ tránh duplicate

//             enrollmentRepository.save(
//                     Enrollment.builder()
//                             .user(order.getUser())
//                             .course(item.getCourse())
//                             .priceAtPurchase(item.getPrice())
//                             .progress(BigDecimal.ZERO)
//                             .status(EnrollmentStatus.ACTIVE)
//                             .enrolledAt(LocalDateTime.now())
//                             .build()
//             );
//         }

//         log.info("Order paid: orderId={}, enrolled {} courses",
//                 orderId, order.getItems().size());
//     }

//     // ================= ADMIN =================

//     public Page<OrderResponse> getAll(Pageable pageable) {
//         return orderRepository.findAll(pageable)
//                 .map(OrderMapper::toResponse);
//     }

//     public long countByUser(Long userId) {
//         return orderRepository.countByUser_Id(userId);
//     }

//     public long countAll() {
//         return orderRepository.count();
//     }
// }