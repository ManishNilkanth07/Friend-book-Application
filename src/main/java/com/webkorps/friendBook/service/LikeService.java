package com.webkorps.friendBook.service;

public interface LikeService {

    void likePost(Long postId);
    void unlikePost(Long postId);
    boolean isPostLikedByCurrentUser(Long postId);
    long getLikeCount(Long postId);
}
