package com.myblogbackend.blog.services;

import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.PostResponse;

import java.util.UUID;

public interface PostService {
    PostResponse createPost(PostRequest postRequest);
    PostResponse getPostById(UUID id);

    PaginationPage<PostResponse> getAllPostsByUserId(Integer offset, Integer limited, UUID userId);
}
