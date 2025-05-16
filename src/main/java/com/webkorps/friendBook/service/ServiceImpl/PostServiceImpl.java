package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.DTO.PostDto;
import com.webkorps.friendBook.exceptions.*;
import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.PostRepository;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.PostService;
import com.webkorps.friendBook.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Override
    public String uploadPostImage(Long postId, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(String.format("Post not found with post Id : %d",postId)));

        if (file.isEmpty()) {
            return "File is empty";
        }
        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "post_" + postId + "_" + System.currentTimeMillis() + fileExtension;

            Path uploadPath = Paths.get("src/main/resources/static/images/uploads/post-images/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String postPhotoUrl = "images/uploads/post-images/" + fileName;

            post.setImageUrl(postPhotoUrl);
            postRepository.save(post);
            return "Post image uploaded successfully: " + postPhotoUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStoreException("Failed to upload post image");
        }
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapToPostDto).toList();
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
