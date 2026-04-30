package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.CourseRequest;
import com.khait_academy.backend.dto.response.CourseResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.enums.CourseStatus;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.CourseMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TeacherRepository teacherRepository;
    private final DiscountSelectionService discountSelectionService;

    // ================= CREATE =================
    public CourseResponse create(CourseRequest request) {

        validateCreateRequest(request);

        if (courseRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Course course = CourseMapper.toEntity(request);
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setStatus(CourseStatus.DRAFT);

        return buildResponse(courseRepository.save(course));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAll(Pageable pageable) {
        return mapToResponsePage(courseRepository.findAll(pageable));
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {

        if (id == null) {
            throw new BadRequestException("Course id must not be null");
        }

        Course course = courseRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return buildResponse(course);
    }

    // ================= UPDATE =================
    public CourseResponse update(Long id, CourseRequest request) {

        if (id == null) {
            throw new BadRequestException("Course id must not be null");
        }

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // slug check
        if (request.getSlug() != null
                && !request.getSlug().equals(course.getSlug())
                && courseRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug already exists");
        }

        CourseMapper.updateEntity(course, request);

        // category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            course.setCategory(category);
        }

        // teacher
        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            course.setTeacher(teacher);
        }

        // status
        if (request.getStatus() != null) {
            try {
                course.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid course status");
            }
        }

        return buildResponse(courseRepository.save(course));
    }

    // ================= DELETE =================
    public void delete(Long id) {

        if (id == null) {
            throw new BadRequestException("Course id must not be null");
        }

        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found");
        }

        courseRepository.deleteById(id);
    }

    // ================= SEARCH =================
    @Transactional(readOnly = true)
    public Page<CourseResponse> search(String keyword, Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {
            return getAll(pageable);
        }

        return mapToResponsePage(
                courseRepository.searchCourses(keyword.trim(), pageable)
        );
    }

    // ================= FILTER =================
    @Transactional(readOnly = true)
    public Page<CourseResponse> filter(
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Double rating,
            Pageable pageable
    ) {

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException("minPrice must be <= maxPrice");
        }

        return mapToResponsePage(
                courseRepository.filterCourses(
                        categoryId,
                        minPrice != null ? BigDecimal.valueOf(minPrice) : null,
                        maxPrice != null ? BigDecimal.valueOf(maxPrice) : null,
                        rating,
                        pageable
                )
        );
    }

    // ================= MAIN GET =================
    @Transactional(readOnly = true)
    public Page<CourseResponse> getCourses(
            String keyword,
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Double rating,
            Pageable pageable
    ) {

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasFilter = categoryId != null || minPrice != null || maxPrice != null || rating != null;

        if (hasKeyword) return search(keyword, pageable);
        if (hasFilter) return filter(categoryId, minPrice, maxPrice, rating, pageable);

        return getAll(pageable);
    }

    // ================= PRIVATE =================

    private void validateCreateRequest(CourseRequest request) {

        if (request == null) {
            throw new BadRequestException("Request must not be null");
        }

        if (request.getCategoryId() == null) {
            throw new BadRequestException("CategoryId is required");
        }

        if (request.getTeacherId() == null) {
            throw new BadRequestException("TeacherId is required");
        }

        if (request.getSlug() == null || request.getSlug().isBlank()) {
            throw new BadRequestException("Slug is required");
        }
    }

    private CourseResponse buildResponse(Course course) {

        if (course.getId() == null) {
            throw new IllegalStateException("Course id is null → cannot calculate price");
        }

        BigDecimal finalPrice = discountSelectionService
                .getFinalPrice(course.getId(), course.getPrice());

        return CourseMapper.toResponse(course, finalPrice);
    }

    private Page<CourseResponse> mapToResponsePage(Page<Course> page) {

        if (page.isEmpty()) {
            return Page.empty(page.getPageable());
        }

        List<CourseResponse> responses = page.getContent()
                .stream()
                .map(this::buildResponse)
                .toList();

        return new PageImpl<>(responses, page.getPageable(), page.getTotalElements());
    }
}