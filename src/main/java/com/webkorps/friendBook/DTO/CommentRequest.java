package com.webkorps.friendBook.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotBlank(message = "Comment text cannot be empty")
    private String comment;
}
