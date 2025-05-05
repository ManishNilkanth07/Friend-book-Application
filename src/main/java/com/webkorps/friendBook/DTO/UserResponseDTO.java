package com.webkorps.friendBook.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private Boolean isPrivate;
    private LocalDateTime createdAt;
}