package com.webkorps.friendBook.controller;

import com.webkorps.friendBook.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
@Tag(name = "Like controller for user" ,description = "API's for managing post's likes and unlikes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likePost/{postId}")
    @Operation(summary = "like post by post Id", description = "Returns HTTPStatus code")
    public ResponseEntity<Void> likePost(@PathVariable Long postId)
    {
        likeService.likePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unlikePost/{postId}")
    @Operation(summary = "Unlike post by post Id", description = "Returns HTTPStatus code")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId)
    {
        likeService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/{postId}")
    @Operation(summary = "get like counts by post Id", description = "Returns number of count and  HTTPStatus code")
    public ResponseEntity<Long> getLikeCounts(@PathVariable Long postId)
    {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "check user like the post or not", description = "Returns boolean response")
    public ResponseEntity<Boolean> isPostLikedByCurrentUser(@PathVariable Long postId)
    {
        return ResponseEntity.ok(likeService.isPostLikedByCurrentUser(postId));
    }
}
