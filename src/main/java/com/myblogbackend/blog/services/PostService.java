package com.myblogbackend.blog.services;

import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.PostResponse;

import java.util.UUID;

public interface PostService {
    PaginationPage<PostResponse> getAllPostsByUserId(Integer offset, Integer limited, UUID userId);

    PostResponse getPostById(UUID id);

    PostResponse createPost(PostRequest postRequest);

    PostResponse updatePost(UUID id, PostRequest postRequest);

}
