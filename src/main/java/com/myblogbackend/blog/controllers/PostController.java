package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.controllers.route.PostRoutes;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.ResponseEntityBuilder;
import com.myblogbackend.blog.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + PostRoutes.BASE_URL)
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/user")
    public ResponseEntity<?> getAllPostsByUserId(@RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                                 @RequestParam(name = "limit", defaultValue = "10") final Integer limit) {
        var postList = postService.getAllPostsByUserId(offset, limit);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(postList)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable(value = "id") final UUID id) {
        var post = postService.getPostById(id);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(post)
                .build();
    }

    @PostMapping()
    public ResponseEntity<?> createPost(@RequestBody @Valid final PostRequest postRequest) {
        var post = postService.createPost(postRequest);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(post)
                .build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable final UUID postId, @RequestBody @Valid final PostRequest request) {
        var post = postService.updatePost(postId, request);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(post)
                .build();
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getAllPostOrderByCreated(@RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                                      @RequestParam(name = "limit", defaultValue = "10") final Integer limit) {
        var postFeeds = postService.getAllPostOrderByCreated(offset, limit);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(postFeeds)
                .build();
    }
    @PutMapping("/disable/{postId}")
    public ResponseEntity<?> disablePost(@PathVariable final UUID postId) {
        postService.disablePost(postId);
        return ResponseEntityBuilder.getBuilder()
                .build();
    }


}
