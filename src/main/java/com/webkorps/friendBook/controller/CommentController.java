package com.webkorps.friendBook.controller;

import com.webkorps.friendBook.DTO.CommentDto;

import com.webkorps.friendBook.DTO.CommentRequest;
import com.webkorps.friendBook.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment controller for user" ,description = "API's for managing comments on post")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    @Operation(summary = "Add comment on post", description = "Returns CommentDto as response")
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentRequest request)
    {

        CommentDto commentDto1 = commentService.addComment(request);
        return new ResponseEntity<>(commentDto1, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get All Comments on post", description = "Returns list of CommentDto as response")
    public ResponseEntity<List<CommentDto>> getAllCommentsOnPostByPostId(@PathVariable Long postId)
    {
      return ResponseEntity.ok(commentService.getComments(postId));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by comment Id", description = "Returns HTTPStatus code")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId)
    {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
