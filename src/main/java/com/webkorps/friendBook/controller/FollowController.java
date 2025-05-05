package com.webkorps.friendBook.controller;

import com.webkorps.friendBook.DTO.UserResponseDTO;
import com.webkorps.friendBook.exceptions.UserNotAuthenticatedException;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
@Tag(name = "Follow controller for user" ,description = "API's for managing user's followings and followers etc")
public class FollowController {

    private final FollowService followService;

    public Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    @PostMapping("/{targetUserId}")
    @Operation(summary = "follow user by target user's Id", description = "Returns success failure message as String")
    public ResponseEntity<String> follow(@PathVariable Long targetUserId) {
        return ResponseEntity.ok(followService.followUser(targetUserId, getCurrentUserId()));
    }

    @DeleteMapping("/{targetUserId}")
    @Operation(summary = "Unfollow user by target user's Id", description = "Returns success failure message as String")
    public ResponseEntity<String> unfollow(@PathVariable Long targetUserId) {
        return ResponseEntity.ok(followService.unfollowUser(targetUserId, getCurrentUserId()));
    }

    @GetMapping("/followers/{userId}")
    @Operation(summary = "Get All user's followers by user Id" , description = "Returns List of user's followers")
    public ResponseEntity<List<UserResponseDTO>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/following/{userId}")
    @Operation(summary = "Get All user's following user by user Id" , description = "Returns List of user's following user")
    public ResponseEntity<List<UserResponseDTO>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}
