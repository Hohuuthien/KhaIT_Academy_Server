package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.DiscountRequest;
import com.khait_academy.backend.dto.response.DiscountResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Discount;
import com.khait_academy.backend.enums.DiscountType;
import com.khait_academy.backend.mapper.DiscountMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.DiscountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final CourseRepository courseRepository;

    // ================= CREATE =================

    public DiscountResponse create(DiscountRequest request) {

        Course course = getCourse(request.getCourseId());

        validateOverlap(course.getId(), request.getStartDate(), request.getEndDate());

        Discount discount = DiscountMapper.toEntity(request, course);

        Discount saved = discountRepository.save(discount);

        log.info("Create discount: courseId={}, value={}, type={}",
                course.getId(), request.getValue(), request.getType());

        return DiscountMapper.toResponse(saved);
    }

    // ================= UPDATE =================

    public DiscountResponse update(Long id, DiscountRequest request) {

        Discount discount = getDiscount(id);

        Course course = getCourse(request.getCourseId());

        boolean overlap = discountRepository.existsOverlappingDiscountExcludeSelf(
                id,
                course.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            throw new RuntimeException("Discount bị trùng thời gian");
        }

        DiscountMapper.update(discount, request, course);

        return DiscountMapper.toResponse(discountRepository.save(discount));
    }

    // ================= DELETE =================

    public void delete(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new RuntimeException("Discount not found");
        }
        discountRepository.deleteById(id);
    }

    // ================= GET =================

    public DiscountResponse getById(Long id) {
        return DiscountMapper.toResponse(getDiscount(id));
    }

    public List<DiscountResponse> getByCourse(Long courseId) {
        return discountRepository.findByCourse_Id(courseId)
                .stream()
                .map(DiscountMapper::toResponse)
                .toList();
    }

    public List<DiscountResponse> getActiveByCourse(Long courseId) {
        return discountRepository.findActiveDiscounts(courseId, LocalDateTime.now())
                .stream()
                .map(DiscountMapper::toResponse)
                .toList();
    }

    // ================= CORE: FINAL PRICE =================

    public BigDecimal getFinalPrice(Long courseId) {

        Course course = getCourse(courseId);

        BigDecimal original = safePrice(course.getPrice());

        Discount best = findBestDiscount(courseId);

        if (best == null || !best.isValidNow()) {
            return original;
        }

        return applyDiscount(original, best);
    }

    public BigDecimal getFinalPrice(Course course) {

        BigDecimal original = safePrice(course.getPrice());

        Discount best = findBestDiscount(course.getId());

        if (best == null || !best.isValidNow()) {
            return original;
        }

        return applyDiscount(original, best);
    }

    // ================= BEST DISCOUNT (OPTIMIZED) =================

    private Discount findBestDiscount(Long courseId) {

        List<Discount> discounts =
                discountRepository.findValidDiscounts(courseId, LocalDateTime.now());

        return discounts.stream()
                .max((a, b) -> compareDiscountValue(a, b))
                .orElse(null);
    }

    private int compareDiscountValue(Discount a, Discount b) {

        BigDecimal valA = calculateDiscountValue(a);
        BigDecimal valB = calculateDiscountValue(b);

        return valA.compareTo(valB);
    }

    // ================= CALCULATION =================

    private BigDecimal calculateDiscountValue(Discount d) {

        if (d.getType() == DiscountType.PERCENT) {
            return d.getValue(); // % so sánh trực tiếp
        }

        // FIXED → cần normalize (giả định scale 1000 để so sánh)
        return d.getValue().multiply(BigDecimal.valueOf(10));
    }

    private BigDecimal applyDiscount(BigDecimal price, Discount d) {

        return switch (d.getType()) {

            case PERCENT -> price.subtract(
                    price.multiply(d.getValue())
                            .divide(BigDecimal.valueOf(100))
            );

            case FIXED -> price.subtract(d.getValue());

            default -> price;
        };
    }

    private BigDecimal safePrice(BigDecimal price) {
        return price == null ? BigDecimal.ZERO : price;
    }

    // ================= VALIDATION =================

    private void validateOverlap(Long courseId,
                                 LocalDateTime start,
                                 LocalDateTime end) {

        if (discountRepository.existsOverlappingDiscount(courseId, start, end)) {
            throw new RuntimeException("Discount bị trùng thời gian");
        }
    }

    // ================= HELPERS =================

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    private Discount getDiscount(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
    }
}