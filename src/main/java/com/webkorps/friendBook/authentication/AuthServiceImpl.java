package com.webkorps.friendBook.authentication;

import com.webkorps.friendBook.exceptions.InvalidCredentialsException;
import com.webkorps.friendBook.exceptions.UserExistsWithUsernameException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.Role;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.security.JwtService;
import com.webkorps.friendBook.utils.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

//    private static final Long EXPIRATION_TIME = (long) (15 * 60 * 1000);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e)
        {
            throw new UserExistsWithUsernameException(String.format("user with username : %d is already exists ",request.getUsername()));
        }
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(token).build();
    }

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()-> new UserNotFoundException(String.format("user not found with username : %d",request.getUsername())));

            String token = jwtService.generateToken(user);
            return AuthenticationResponse.builder().accessToken(token).build();
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @Override
    public String forgotPassword(String email, String username) throws MessagingException {
        User user = userRepository.findByUsernameAndEmail(username,email)
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with username : %d and email : %d",username, email)));

        String token = jwtService.generatePasswordResetToken(user);
        String resetLink = "http://localhost:8081/reset-password?token=" + token;
        return emailService.sendPasswordResetEmail(email,resetLink);

    }

    @Override
    public void updatePassword(String token, String newPassword) {
        Map<String ,Object> claims = jwtService.getAllClaims(token);
        String username = claims.get("username").toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with username : %d ",username)));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
