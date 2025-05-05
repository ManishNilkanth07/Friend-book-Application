package com.webkorps.friendBook.service;

import com.webkorps.friendBook.DTO.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto request);

    PostDto updatePost(Long postId, PostDto request);

    void deletePost(Long postId);

    PostDto getPostById(Long postId);

    List<PostDto> getPostsByUser(String username);

}
