package com.webkorps.friendBook.service;

import com.webkorps.friendBook.DTO.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto request);

    PostDto updatePost(Long postId, PostDto request);

    void deletePost(Long postId);

    PostDto getPostById(Long postId);

    List<PostDto> getPostsByUser(String username);

    String uploadPostImage(Long postId, MultipartFile file);

    List<PostDto> getAllPosts();
}
