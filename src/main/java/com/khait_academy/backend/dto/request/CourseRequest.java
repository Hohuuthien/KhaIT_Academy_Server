package com.khait_academy.backend.dto.request;

import lombok.Data;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private Double price;
    private String thumbnail;
}
