package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.entities.Category;
import com.khait_academy.backend.mapper.CategoryMapper;
import com.khait_academy.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * CREATE
     */
    @Transactional
    public CategoryResponse create(CategoryRequest request) {

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        Category category = CategoryMapper.toEntity(request);

        // set parent
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponse(saved);
    }

    /**
     * GET ALL ROOT (tree level 1)
     * ⚠️ full tree nên build service nếu cần sâu hơn
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findByParentIsNull()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    /**
     * GET BY ID
     */
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return CategoryMapper.toResponse(category);
    }

    /**
     * GET CHILDREN ONLY
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getChildren(Long parentId) {

        if (!categoryRepository.existsById(parentId)) {
            throw new RuntimeException("Category not found");
        }

        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    /**
     * UPDATE
     */
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getSlug().equals(request.getSlug())
                && categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        CategoryMapper.updateEntity(category, request);

        // parent update
        if (request.getParentId() != null) {

            if (request.getParentId().equals(id)) {
                throw new RuntimeException("Category cannot be its own parent");
            }

            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));

            category.setParent(parent);

        } else {
            category.setParent(null);
        }

        Category updated = categoryRepository.save(category);
        return CategoryMapper.toResponse(updated);
    }

    /**
     * DELETE (SAFE VERSION)
     */
    @Transactional
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // check children bằng query thay vì lazy load
        List<Category> children = categoryRepository.findByParentId(id);

        if (!children.isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }

        categoryRepository.delete(category);
    }
}