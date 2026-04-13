package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // ROOT categories + children 1 level
    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentIsNull();

    // subcategories
    List<Category> findByParentId(Long parentId);

    // SAFE: fetch parent + children (1 level)
    @EntityGraph(attributePaths = {"parent", "children"})
    Optional<Category> findById(Long id);

    // OPTIONAL: nếu bạn muốn FULL TREE (khuyên không dùng EntityGraph)
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findAllRoot();
}