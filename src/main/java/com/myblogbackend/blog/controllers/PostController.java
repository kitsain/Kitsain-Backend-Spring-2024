package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.controllers.route.PostRoutes;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.PostResponse;
import com.myblogbackend.blog.response.ResponseEntityBuilder;
import com.myblogbackend.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + PostRoutes.BASE_URL)
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable(value = "id") final UUID id) {
        var post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getAllPostsByUserId(@RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                                 @RequestParam(name = "limit", defaultValue = "10") final Integer limit, @PathVariable final UUID userId) {
        var postList = postService.getAllPostsByUserId(offset, limit, userId);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(postList)
                .build();
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody final PostRequest postRequest) {
        PostResponse post = postService.createPost(postRequest);
        return ResponseEntity.ok(post);
    }

}
