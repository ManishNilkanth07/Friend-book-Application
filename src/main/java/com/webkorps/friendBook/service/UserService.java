package com.webkorps.friendBook.service;

import com.webkorps.friendBook.DTO.UserUpdateRequest;
import com.webkorps.friendBook.DTO.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public UserResponseDTO getProfile(Long userId);

    public UserResponseDTO updateProfile(Long userId, UserUpdateRequest request);

    public List<UserResponseDTO> searchUsers(String keyword);

    public void deleteUser(Long userId);

    String updateProfilePhoto(Long userId, MultipartFile file);
}
