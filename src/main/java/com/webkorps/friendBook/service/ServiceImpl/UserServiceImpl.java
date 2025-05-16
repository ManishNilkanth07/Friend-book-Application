package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.DTO.UserUpdateRequest;
import com.webkorps.friendBook.DTO.UserResponseDTO;
import com.webkorps.friendBook.exceptions.FileStoreException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.format("User not found with given user Id: %d", userId)));

        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO updateProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.format("User not found with given user Id: %d", userId)));

        modifyProfile(user, request);

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    private void modifyProfile(User user, UserUpdateRequest request) {
        if(request.getFirstName() != null)
        {
            user.setFirstName(request.getFirstName());
        }
        if(request.getLastName() != null)
        {
           user.setLastName(request.getLastName());
        }
        if(request.getEmail() != null)
        {
            user.setEmail(request.getEmail());
        }
        if(request.getBio() != null)
        {
            user.setBio(request.getBio());
        }
        if(request.getIsPrivate() != null)
        {
            user.setIsPrivate(request.getIsPrivate());
        }
        if(request.getProfilePictureUrl() != null)
        {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }
    }

    @Override
    public List<UserResponseDTO> searchUsers(String keyword) {
        List<User> userList = userRepository.findByUsernameContainingIgnoreCase(keyword);
        return userList.stream().map(this::mapToDTO).toList();
    }

    @Override
    public void deleteUser(Long userId) {

        if(!userRepository.existsById(userId))
        {
            throw new UserNotFoundException(String.format("User not found with given user Id: %d", userId));
        }
        userRepository.deleteById(userId);
    }

    @Override
    public String updateProfilePhoto(Long userId, MultipartFile file) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException(String.format("User not found with given user Id: %d", userId)));

            if (file.isEmpty()) {
                return "File is empty";
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "user_" + userId + "_" + System.currentTimeMillis() + fileExtension;


            Path uploadPath = Paths.get("src/main/resources/static/images/uploads/profile_pictures/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String profilePhotoUrl = "images/uploads/profile_pictures/" + fileName;

            user.setProfilePictureUrl(profilePhotoUrl);
            userRepository.save(user);
            return "Profile photo uploaded successfully: " + profilePhotoUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStoreException("Failed to upload profile photo");
        }
    }

    private UserResponseDTO mapToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .isPrivate(user.getIsPrivate())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
