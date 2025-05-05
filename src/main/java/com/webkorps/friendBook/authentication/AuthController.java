package com.webkorps.friendBook.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication controller for user",description = "API's for authenticating the user and forgot, reset password")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register user by RegisterRequest object in body", description = "Returns the JWT token as response")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest request)
    {
        return new ResponseEntity<>(authService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user by LoginRequest object in body" , description = "Returns the JWT token as response")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest request)
    {
        return new ResponseEntity<>(authService.loginUser(request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Get reset password link by email and username in param" , description = "Returns the success message and mail the reset password link")
    public ResponseEntity<String> forgotPassword(@RequestParam String email, @RequestParam String username) throws MessagingException {
        return new ResponseEntity<>(authService.forgotPassword(email,username),HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password by token and new password in param" , description = "Successfully reset the password and returns HTTPStatus code")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        authService.updatePassword(token,newPassword);
        return ResponseEntity.noContent().build();
    }
}
