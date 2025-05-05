package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.DTO.UserResponseDTO;
import com.webkorps.friendBook.exceptions.UserNotAuthenticatedException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.Follower;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.FollowerRepository;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.FollowService;
import com.webkorps.friendBook.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final SecurityUtil securityUtil;




    @Override
    public String followUser(Long targetUserId, Long currentUserId) {

        if (targetUserId.equals(currentUserId)) {
            throw new RuntimeException("You cannot follow yourself.");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Current user not found with user Id : %d",currentUserId)));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Target user not found with userId : %d",targetUserId)));

        boolean alreadyFollowing = followerRepository.findByFollowerAndFollowing(currentUser, targetUser).isPresent();
        if (alreadyFollowing) {
            return "Already following this user.";
        }

        Follower follower = Follower.builder()
                .follower(currentUser)
                .following(targetUser)
                .followedAt(LocalDateTime.now())
                .build();

        followerRepository.save(follower);
        return "Successfully followed " + targetUser.getUsername();
    }

    @Override
    public String unfollowUser(Long targetUserId, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Current user not found with user Id : %d",currentUserId)));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Target user not found with userId : %d",targetUserId)));

        Follower follower = followerRepository.findByFollowerAndFollowing(currentUser, targetUser)
                .orElseThrow(() -> new RuntimeException(String.format("You are not following this user with user id :%d",targetUser)));

        followerRepository.delete(follower);
        return "Unfollowed " + targetUser.getUsername();
    }

    @Override
    public List<UserResponseDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        return followerRepository.findByFollowing(user).stream()
                .map(followingUser -> mapToDTO(followingUser.getFollower()))
                .toList();
    }

    @Override
    public List<UserResponseDTO> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Current user not found with user Id : %d",userId)));

        return followerRepository.findByFollower(user).stream()
                .map(followerUser -> mapToDTO(followerUser.getFollowing()))
                .toList();
    }

    @Override
    public Long getFollowersCount() {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with user id : %d",securityUtil.getCurrentUserId())));

        return followerRepository.countByFollower(user);
    }

    @Override
    public Long getFollowingCount() {
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with user id : %d",securityUtil.getCurrentUserId())));

        return followerRepository.countByFollowing(user);
    }


    private UserResponseDTO mapToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .isPrivate(user.getIsPrivate())
                .build();
    }
}
