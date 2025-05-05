package com.webkorps.friendBook.service.ServiceImpl;

import com.webkorps.friendBook.DTO.CommentDto;
import com.webkorps.friendBook.DTO.CommentRequest;
import com.webkorps.friendBook.exceptions.CommentNotFoundException;
import com.webkorps.friendBook.exceptions.PostNotFoundException;
import com.webkorps.friendBook.exceptions.UserNotFoundException;
import com.webkorps.friendBook.model.Comment;
import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import com.webkorps.friendBook.repository.CommentRepository;
import com.webkorps.friendBook.repository.PostRepository;
import com.webkorps.friendBook.repository.UserRepository;
import com.webkorps.friendBook.service.CommentService;
import com.webkorps.friendBook.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;


    @Override
    public CommentDto addComment(CommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(()-> new PostNotFoundException(String.format("post not found with post Id: %d",request.getPostId())));

        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException(String.format("User not found with given user Id: %d", securityUtil.getCurrentUserId())));

        Comment addComment = Comment.builder()
                .comment(request.getComment())
                .post(post)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(addComment);

        return mapToCommentDTO(savedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        if(!commentRepository.existsById(commentId))
        {
            throw new CommentNotFoundException(String.format("Comment not found with comment id : %d",commentId));
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getComments(Long postId) {
        if(!postRepository.existsById(postId))
        {
            throw  new PostNotFoundException(String.format("post not found with post Id: %d",postId));
        }
        Optional<List<Comment>> comments = commentRepository.findByPostId(postId);
        return comments
                .map(list -> list.stream()
                        .map(this::mapToCommentDTO)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private CommentDto mapToCommentDTO(Comment savedComment) {
        return CommentDto.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .createdAt(savedComment.getCreatedAt())
                .postId(savedComment.getPost().getId())
                .build();
    }
}
