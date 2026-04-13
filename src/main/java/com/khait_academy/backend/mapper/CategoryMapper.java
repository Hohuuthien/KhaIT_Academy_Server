package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    // Request -> Entity
    public static Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .build();
    }

    // Entity -> Response (SAFE version)
    public static CategoryResponse toResponse(Category category) {
        if (category == null) return null;

        CategoryResponse response = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .children(new ArrayList<>())
                .build();

        // map children an toàn
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryResponse> children = category.getChildren()
                    .stream()
                    .map(CategoryMapper::toResponse)
                    .toList();

            response.setChildren(new ArrayList<>(children));
        }

        return response;
    }

    // Update entity
    public static void updateEntity(Category category, CategoryRequest request) {
        if (request == null || category == null) return;

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
    }
}