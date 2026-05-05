package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.entities.Category;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.CategoryMapper;
import com.khait_academy.backend.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ================= CREATE =================
    public CategoryResponse create(CategoryRequest request) {

        validateSlugUnique(request.getSlug(), null);

        Category category = CategoryMapper.toEntity(request);

        category.setParent(resolveParent(request.getParentId(), null));

        return CategoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    // ================= GET ALL ROOT =================
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return categoryRepository.findByParentIsNull()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return CategoryMapper.toResponse(getCategory(id));
    }

    // ================= GET CHILDREN =================
    @Transactional(readOnly = true)
    public List<CategoryResponse> getChildren(Long parentId) {

        getCategory(parentId); // validate tồn tại

        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    // ================= UPDATE =================
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = getCategory(id);

        validateSlugUnique(request.getSlug(), id);

        CategoryMapper.updateEntity(category, request);

        category.setParent(resolveParent(request.getParentId(), id));

        return CategoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Category category = getCategory(id);

        if (categoryRepository.existsByParentId(id)) {
            throw new BadRequestException("Cannot delete category with subcategories");
        }

        categoryRepository.delete(category);
    }

    // ================= HELPER =================

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private void validateSlugUnique(String slug, Long excludeId) {

        boolean exists = (excludeId == null)
                ? categoryRepository.existsBySlug(slug)
                : categoryRepository.existsBySlugAndIdNot(slug, excludeId);

        if (exists) {
            throw new BadRequestException("Slug already exists");
        }
    }

    private Category resolveParent(Long parentId, Long currentId) {

        if (parentId == null) return null;

        if (parentId.equals(currentId)) {
            throw new BadRequestException("Category cannot be its own parent");
        }

        Category parent = getCategory(parentId);

        // 🔥 CHECK CYCLE (quan trọng)
        if (isCycle(parent, currentId)) {
            throw new BadRequestException("Invalid parent (cycle detected)");
        }

        return parent;
    }

    private boolean isCycle(Category parent, Long currentId) {

        while (parent != null) {
            if (parent.getId().equals(currentId)) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }
}