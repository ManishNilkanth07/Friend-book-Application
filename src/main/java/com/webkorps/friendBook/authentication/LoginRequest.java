package com.webkorps.friendBook.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotNull(message = "username should not be null")
    @NotBlank(message = "username should not be blank")
    private String username;

    @NotNull(message = "password should not be null")
    @NotBlank(message = "password should not be blank")
    private String password;
}