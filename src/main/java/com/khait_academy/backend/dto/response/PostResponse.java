package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String slug;
    private String thumbnail;
    private String content;

    private Long authorId;
    private String authorName;

    private Long categoryId;
    private String categoryName;

    private PostStatus status;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    
}