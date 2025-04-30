package com.webkorps.friendBook.authentication;

import jakarta.mail.MessagingException;

public interface AuthService {

    public AuthenticationResponse registerUser(RegisterRequest request);

    public AuthenticationResponse loginUser(LoginRequest request);

    public String forgotPassword(String email, String username) throws MessagingException;

    public void updatePassword(String token, String newPassword);

}
