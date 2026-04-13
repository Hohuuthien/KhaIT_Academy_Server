package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.PostStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    private String title;
    private String slug;
    private String thumbnail;
    private String content;
    private Long categoryId;
    private PostStatus status;
}