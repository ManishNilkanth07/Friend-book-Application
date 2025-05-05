package com.webkorps.friendBook.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

 @Data
 @Builder
public class PostDto {
    private Long id;

    @NotBlank(message = "caption should not be blank")
    private String caption;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    private String username;
}

