package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.PostResponse;
import com.khait_academy.backend.entities.Post;

public class PostMapper {

    public static PostResponse toResponse(Post post) {

        if (post == null) return null;

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .thumbnail(post.getThumbnail())
                .content(post.getContent())

                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getFullName() : null)

                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)

                .status(post.getStatus())

                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())

                .build();
    }
}