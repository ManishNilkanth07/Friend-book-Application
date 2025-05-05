package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.exceptions.PostNotFoundException;
import com.webkorps.friendBook.exceptions.UserNotAuthenticatedException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.Like;
import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.LikeRepository;
import com.webkorps.friendBook.repository.PostRepository;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.LikeService;
import com.webkorps.friendBook.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;

    @Override
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("post not found with post id: %d",postId)));

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with user id : %d",securityUtil.getCurrentUserId())));

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        likeRepository.save(like);
    }

    @Transactional //todo need to work on it
    @Override
    public void unlikePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("post not found with post id: %d",postId)));

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with user id : %d",securityUtil.getCurrentUserId())));

        likeRepository.deleteByUserAndPost(user,post);
    }

    @Override
    public boolean isPostLikedByCurrentUser(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("post not found with post id: %d",postId)));

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with user id : %d",securityUtil.getCurrentUserId())));

        return likeRepository.existsByUserAndPost(user,post);
    }

    @Override
    public long getLikeCount(Long postId) {
        if(!postRepository.existsById(postId))
        {
           throw new PostNotFoundException(String.format("post not found with post id: %d",postId));
        }
        return likeRepository.countByPostId(postId);
    }
}
