package com.webkorps.friendBook.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @NotNull(message = "user name should not be null")
    @NotBlank(message = "user name should not be blank")
    private String firstName;

    @NotNull(message = "user surname should not be null")
    @NotBlank(message = "user surname should not be blank")
    private String lastName;

    @NotNull(message = "username should not be null")
    @NotBlank(message = "username should not be blank")
    private String username;

    @NotNull(message = "password should not be null")
    @NotBlank(message = "password should not be blank")
    private String password;

    @NotNull(message = "email name should not be null")
    @NotBlank(message = "email name should not be blank")
    @Email(message = "invalid email formate")
    private String email;

}
