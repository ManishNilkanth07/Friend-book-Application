package com.webkorps.friendBook.controller;

import com.webkorps.friendBook.DTO.PostDto;
import com.webkorps.friendBook.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Tag(name = "Post controller for user",description = "API's for managing user's posts")
public class PostController {

    private final PostService postService;

    @PostMapping()
    @Operation(summary = "Create post by PostDto object in body", description = "Returns the PostDto object as response")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get post by post Id", description = "Returns the PostDto object as response")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId)
    {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping("user/{username}")
    @Operation(summary = "Get all post by username", description = "Returns the list of PostDto object as response")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable String username)
    {
        return ResponseEntity.ok(postService.getPostsByUser(username));
    }

    @GetMapping()
    @Operation(summary = "Get all posts", description = "Returns the list of PostDto object as response")
    public ResponseEntity<List<PostDto>> getAllPosts()
    {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Update post by post Id", description = "Returns the PostDto object as response")
    public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postDto,@PathVariable Long postId)
    {
        return ResponseEntity.ok(postService.updatePost(postId,postDto));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete post by post Id", description = "Successfully delete the user and returns HTTPStatus code")
    public ResponseEntity<Void> deletePostById(@PathVariable Long postId)
    {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/uploadImage")
    @Operation(summary = "Upload image for a post", description = "Uploads and returns image URL")
    public ResponseEntity<String> uploadPostImage(@PathVariable Long postId, @RequestParam("file") MultipartFile file) {
        String imageUrl = postService.uploadPostImage(postId, file);
        return ResponseEntity.ok(imageUrl);
    }
}
