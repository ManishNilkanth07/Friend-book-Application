package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.DTO.PostDto;
import com.webkorps.friendBook.exceptions.PostNotFoundException;
import com.webkorps.friendBook.exceptions.UserNotAuthenticatedException;
import com.webkorps.friendBook.exceptions.UserNotAuthorizedException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.PostRepository;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.PostService;
import com.webkorps.friendBook.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    @Override
    public PostDto createPost(PostDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()-> new UserNotFoundException(String.format("user not found with username : %s",dto.getUsername())));

        Post post = Post.builder()
                .caption(dto.getCaption())
                .imageUrl(dto.getImageUrl())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);

        return mapToPostDto(savedPost);
    }

    @Override
    public PostDto updatePost(Long postId, PostDto request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("Post not found with post Id : %d",postId)));

        if(!post.getUser().getId().equals(securityUtil.getCurrentUserId()))
        {
            throw new UserNotAuthorizedException("User can not update others user's post");
        }
        if(request.getCaption() != null)
        {
            post.setCaption(request.getCaption());
        }
        if(request.getImageUrl() != null)
        {
            post.setImageUrl(request.getImageUrl());
        }
        Post savedPost = postRepository.save(post);
        return mapToPostDto(post);
    }



    @Override
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("Post not found with post Id : %d",postId)));

        if(!post.getUser().getId().equals(securityUtil.getCurrentUserId()))
        {
            throw new UserNotAuthorizedException("User can not delete others user's post");
        }
        if(!postRepository.existsById(postId))
        {
            throw new PostNotFoundException(String.format("Post not found with post Id : %d",postId));
        }
        postRepository.deleteById(postId);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("Post not found with post Id : %d",postId)));
        return mapToPostDto(post);
    }

    @Override
    public List<PostDto> getPostsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(String.format("User not found with username : %s",username)));

        Optional<List<Post>> posts = postRepository.findByUser(user);

        return posts
                .map(list -> list.stream()
                        .map(this::mapToPostDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }



    public PostDto mapToPostDto(Post post)
    {
        return PostDto.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .createdAt(post.getCreatedAt())
                .lastUpdatedAt(post.getLastUpdatedAt())
                .imageUrl(post.getImageUrl())
                .username(post.getUser().getUsername())
                .build();
    }
}
