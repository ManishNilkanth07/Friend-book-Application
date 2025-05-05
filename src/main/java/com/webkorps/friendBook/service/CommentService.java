package com.webkorps.friendBook.service;

import com.webkorps.friendBook.DTO.CommentDto;
import com.webkorps.friendBook.DTO.CommentRequest;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentRequest request);
    void deleteComment(Long commentId);
    List<CommentDto> getComments(Long postId);
}
