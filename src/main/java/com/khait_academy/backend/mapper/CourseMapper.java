package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.CourseRequest;
import com.khait_academy.backend.dto.response.CourseResponse;
import com.khait_academy.backend.entities.Course;

public class CourseMapper {

    /**
     * ✅ Request → Entity
     */
    public static Course toEntity(CourseRequest request) {
        return Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .thumbnail(request.getThumbnail())
                .build();
    }

    /**
     * ✅ Entity → Response
     */
    public static CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .thumbnail(course.getThumbnail())
                .build();
    }

    /**
     * ✅ Update Entity từ Request
     */
    public static void updateEntity(Course course, CourseRequest request) {
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setThumbnail(request.getThumbnail());
    }
}