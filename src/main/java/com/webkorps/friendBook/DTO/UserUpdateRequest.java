package com.webkorps.friendBook.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String bio;

    private String profilePicture;

    private Boolean isPrivate;
}
