package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.controllers.route.CommentRoutes;
import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.request.CommentRequest;
import com.myblogbackend.blog.response.ResponseEntityBuilder;
import com.myblogbackend.blog.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + CommentRoutes.BASE_URL)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<?> getListCommentsByPostId(
            @RequestParam(name = "limit", defaultValue = "10") final Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") final Integer offset,
            @PathVariable(value = "postId") final UUID postId) {
        var commentResponseList = commentService.getListCommentsByPostId(offset, limit, postId);
        return ResponseEntity.ok(commentResponseList);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Valid final CommentRequest commentRequest) {
        var commentResponse = commentService.createNewComment(commentRequest);
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(commentResponse)
                .build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updatePost(@PathVariable final UUID commentId,
                                        @RequestBody @Valid final CommentRequest request) {
        var post = commentService.updateComment(commentId, request);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(post)
                .build();
    }
}
