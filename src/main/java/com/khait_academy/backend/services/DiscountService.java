package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.DiscountRequest;
import com.khait_academy.backend.dto.response.DiscountResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Discount;
import com.khait_academy.backend.enums.DiscountType;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.DiscountMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.DiscountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final CourseRepository courseRepository;
    private final DiscountSelectionService discountSelectionService; // ✅ dùng service này

    // ================= CREATE =================
    public DiscountResponse create(DiscountRequest request) {

        Course course = getCourse(request.getCourseId());

        validateRequest(request);
        validateOverlap(course.getId(), request.getStartDate(), request.getEndDate());

        Discount discount = DiscountMapper.toEntity(request, course);

        if (discount.getIsActive() == null) {
            discount.setIsActive(true);
        }

        log.info("Create discount: courseId={}, value={}, type={}",
                course.getId(), request.getValue(), request.getType());

        return DiscountMapper.toResponse(discountRepository.save(discount));
    }

    // ================= UPDATE =================
    public DiscountResponse update(Long id, DiscountRequest request) {

        Discount discount = getDiscount(id);
        Course course = getCourse(request.getCourseId());

        validateRequest(request);

        boolean overlap = discountRepository.existsOverlappingDiscountExcludeSelf(
                id,
                course.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            throw new BadRequestException("Discount time overlap");
        }

        DiscountMapper.update(discount, request, course);

        return DiscountMapper.toResponse(discountRepository.save(discount));
    }

    // ================= DELETE =================
    public void delete(Long id) {
        Discount discount = getDiscount(id);
        discountRepository.delete(discount);
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

    // ================= FINAL PRICE =================
    public BigDecimal getFinalPrice(Long courseId) {

        Course course = getCourse(courseId);

        return discountSelectionService.getFinalPrice(
                course.getId(),
                course.getPrice()
        );
    }

    // ================= HELPERS =================
    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Discount getDiscount(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
    }

    // ================= VALIDATION =================
    private void validateRequest(DiscountRequest r) {

        if (r.getStartDate() == null || r.getEndDate() == null) {
            throw new BadRequestException("Start date and end date are required");
        }

        if (r.getStartDate().isAfter(r.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }

        if (r.getValue() == null || r.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Discount value must be positive");
        }

        if (r.getType() == null) {
            throw new BadRequestException("Discount type is required");
        }

        if (r.getType() == DiscountType.PERCENT &&
                r.getValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BadRequestException("Percent cannot exceed 100");
        }
    }

    private void validateOverlap(Long courseId,
                                 LocalDateTime start,
                                 LocalDateTime end) {

        if (discountRepository.existsOverlappingDiscount(courseId, start, end)) {
            throw new BadRequestException("Discount time overlap");
        }
    }
}