package com.webkorps.friendBook.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {

    @NotNull(message = "firstName should not be null")
    @NotBlank(message = "firstName should not be blank")
    private String firstName;

    @NotNull(message = "lastName should not be null")
    @NotBlank(message = "lastName should not be blank")
    private String lastName;

    @NotNull(message = "email should not be null")
    @NotBlank(message = "email should not be blank")
    @Email(message = "invalid email format")
    private String email;

    @NotNull(message = "bio should not be null")
    @NotBlank(message = "bio should not be blank")
    private String bio;

    @NotNull(message = "profilePictureUrl should not be null")
    @NotBlank(message = "profilePictureUrl should not be blank")
    private String profilePictureUrl;

    private Boolean isPrivate;
}
