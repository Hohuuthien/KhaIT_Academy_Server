package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // ================= BASIC =================

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    // ================= TREE =================

    // ROOT categories + children (1 level)
    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentIsNull();

    // children
    List<Category> findByParentId(Long parentId);

    boolean existsByParentId(Long parentId);

    // ================= FETCH CUSTOM =================

    // Khi cần fetch parent + children (explicit, KHÔNG override findById)
    @EntityGraph(attributePaths = {"parent", "children"})
    Optional<Category> findWithParentAndChildrenById(Long id);
}