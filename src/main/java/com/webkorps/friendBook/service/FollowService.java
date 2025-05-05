package com.webkorps.friendBook.service;

import com.webkorps.friendBook.DTO.UserResponseDTO;

import java.util.List;

public interface FollowService {
    public String followUser(Long targetUserId, Long currentUserId);

    public String unfollowUser(Long targetUserId, Long currentUserId);

    public List<UserResponseDTO> getFollowers(Long userId);

    public List<UserResponseDTO> getFollowing(Long userId);

    public Long getFollowersCount();

    public Long getFollowingCount();
}
