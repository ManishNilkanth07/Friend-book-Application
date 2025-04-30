package com.webkorps.friendBook.authentication;

import com.webkorps.friendBook.exceptions.UserExistsWithUsernameException;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @Override
    public Long registerUser(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        try{
            User savedUser = userRepository.save(user);
            return savedUser.getId();
        }catch (DataIntegrityViolationException e)
        {
            throw new UserExistsWithUsernameException("user with this username is already exists ");
        }
    }
}
