package com.myblogbackend.blog.services;

import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.request.CommentRequest;
import com.myblogbackend.blog.response.CommentResponse;

import java.util.UUID;

public interface CommentService {
    CommentResponse createNewComment(CommentRequest commentRequest);

    CommentResponse updateComment(UUID commentId, CommentRequest commentRequest);

    PaginationPage<CommentResponse> getListCommentsByPostId(Integer offset, Integer limited, UUID postId);
}
