package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.DiscountRequest;
import com.khait_academy.backend.dto.response.DiscountResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Discount;

public class DiscountMapper {

    // ================= CREATE =================
    public static Discount toEntity(DiscountRequest request, Course course) {

        Boolean isActive = request.getIsActive() != null
                ? request.getIsActive()
                : true; 

        return Discount.builder()
                .course(course)
                .value(request.getValue())
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(isActive) 
                .build();
    }

    // ================= UPDATE =================
    public static void update(Discount discount, DiscountRequest request, Course course) {

        discount.setCourse(course);
        discount.setValue(request.getValue());
        discount.setType(request.getType());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());

        if (request.getIsActive() != null) {
            discount.setIsActive(request.getIsActive());
        }
    }

    // ================= RESPONSE =================
    public static DiscountResponse toResponse(Discount discount) {

        return DiscountResponse.builder()
                .id(discount.getId())
                .courseId(discount.getCourse().getId())
                .courseTitle(discount.getCourse().getTitle())
                .value(discount.getValue())
                .type(discount.getType())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .isActive(discount.getIsActive())
                .isValidNow(discount.isValidNow())
                .createdAt(discount.getCreatedAt())
                .build();
    }
}