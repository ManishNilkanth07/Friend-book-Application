package com.webkorps.friendBook.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Data
@Builder
public class CommentDto {
    private Long id;

    private Long postId;

    @NotNull(message = "comment should not be null")
    @NotBlank(message = "comment should not be blank")
    private String comment;

    private Timestamp createdAt;
}
