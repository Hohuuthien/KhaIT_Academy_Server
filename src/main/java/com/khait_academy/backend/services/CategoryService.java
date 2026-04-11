package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.entities.Category;
import com.khait_academy.backend.mapper.CategoryMapper;
import com.khait_academy.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * ✅ CREATE
     */
    public CategoryResponse create(CategoryRequest request) {

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        Category category = CategoryMapper.toEntity(request);

        categoryRepository.save(category);

        return CategoryMapper.toResponse(category);
    }

    /**
     * ✅ GET ALL
     */
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    /**
     * ✅ GET BY ID
     */
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return CategoryMapper.toResponse(category);
    }

    /**
     * ✅ UPDATE
     */
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryMapper.updateEntity(category, request);

        categoryRepository.save(category);

        return CategoryMapper.toResponse(category);
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}