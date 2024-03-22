package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.mapper.PostMapper;
import com.myblogbackend.blog.models.PostEntity;
import com.myblogbackend.blog.pagination.OffsetPageRequest;
import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.repositories.PostRepository;
import com.myblogbackend.blog.repositories.UsersRepository;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.PostResponse;
import com.myblogbackend.blog.services.PostService;
import com.myblogbackend.blog.utils.GsonUtils;
import com.myblogbackend.blog.utils.JWTSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final PostMapper postMapper;

    @Transactional
    @Override
    public PostResponse createPost(final PostRequest postRequest) {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var postEntity = postMapper.toPostEntity(postRequest);
            postEntity.setUser(usersRepository.findById(signedInUser.getId()).orElseThrow());
            var createdPost = postRepository.save(postEntity);
            logger.info("Post was created with id: {}", createdPost.getId());
            return postMapper.toPostResponse(createdPost);
        } catch (Exception e) {
            logger.error("Failed to create post", e);
            throw new RuntimeException("Failed to create post testing");
        }
    }

    @Override
    public PaginationPage<PostResponse> getAllPostsByUserId(final Integer offset, final Integer limited) {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var pageable = new OffsetPageRequest(offset, limited);
            var postEntities = postRepository.findAllByUserId(signedInUser.getId(), pageable);
            logger.info("Post get succeeded with offset: {} and limited {}", postEntities.getNumber(), postEntities.getSize());
            return getPostResponsePaginationPage(postEntities);
        } catch (Exception e) {
            logger.error("Failed to get list post", e);
            throw new RuntimeException("Failed to get list post");
        }
    }
    @Transactional
    @Override
    public PostResponse updatePost(final UUID postId, final PostRequest postRequest) {
        try {
            var post = postRepository
                    .findById(postId)
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setPrice(postRequest.getPrice());
            post.setImages(GsonUtils.arrayToString(postRequest.getImages()));
            post.setExpringDate(postRequest.getExpringDate());
            var updatedPost = postRepository.save(post);
            logger.info("Updated post with id: {}", updatedPost.getId());
            return postMapper.toPostResponse(updatedPost);
        } catch (Exception e) {
            logger.error("Failed to update post", e);
            throw new RuntimeException("Failed to updated post testing");
        }
    }
    @Override
    public PaginationPage<PostResponse> getAllPostOrderByCreated(final Integer offset, final Integer limited) {
        try {
            var pageable = new OffsetPageRequest(offset, limited);
            var postEntities = postRepository.findAllOrderByCreatedDateDesc(pageable);
            logger.info("Get feed list succeeded with offset: {} and limited {}", postEntities.getNumber(), postEntities.getSize());
            return getPostResponsePaginationPage(postEntities);
        } catch (Exception e) {
            logger.error("Failed to get feed list", e);
            throw new RuntimeException("Failed to get feed list");
        }
    }
    @Override
    public PostResponse getPostById(final UUID id) {
        try {
            var post = postRepository
                    .findById(id)
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
            logger.error("Get post successfully by id {} ", id);
            return postMapper.toPostResponse(post);
        } catch (Exception e) {
            logger.error("Failed to get post by id", e);
            throw new RuntimeException("Failed to get post by id");
        }
    }

    private PaginationPage<PostResponse> getPostResponsePaginationPage(final Page<PostEntity> postEntities) {
        var postResponses = postEntities.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        return new PaginationPage<PostResponse>()
                .setRecords(postResponses)
                .setOffset(postEntities.getNumber())
                .setLimit(postEntities.getSize())
                .setTotalRecords(postEntities.getTotalElements());
    }
}
