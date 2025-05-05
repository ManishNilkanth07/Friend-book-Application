package com.webkorps.friendBook.controller;

import com.webkorps.friendBook.DTO.UserResponseDTO;
import com.webkorps.friendBook.DTO.UserUpdateRequest;
import com.webkorps.friendBook.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by user ID", description = "Returns a single user")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user by user ID and updated data in body", description = "Returns user's updated details")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId,@Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by user ID", description = "Successfully delete the user and returns HTTPStatus code")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search user by username keyword", description = "Returns the List of users similar to keyword")
    public ResponseEntity<List<UserResponseDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }
}
