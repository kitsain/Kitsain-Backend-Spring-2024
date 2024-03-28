package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.enums.RatingType;
import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.mapper.PostMapper;
import com.myblogbackend.blog.mapper.UserMapper;
import com.myblogbackend.blog.models.FavoriteEntity;
import com.myblogbackend.blog.pagination.OffsetPageRequest;
import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.repositories.CommentRepository;
import com.myblogbackend.blog.repositories.FavoriteRepository;
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
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public PostResponse createPost(final PostRequest postRequest) {
        var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
        var postEntity = postMapper.toPostEntity(postRequest);
        postEntity.setUser(usersRepository.findById(signedInUser.getId()).orElseThrow());
        postEntity.setStatus(true);
        postEntity.setFavourite(0L);
        postEntity.setCreatedBy(signedInUser.getName());
        var createdPost = postRepository.save(postEntity);
        logger.info("Post was created with id: {}", createdPost.getId());
        return postMapper.toPostResponse(createdPost);

    }

    @Override
    public PaginationPage<PostResponse> getAllPostsByUserId(final Integer offset, final Integer limited) {
        var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
        var pageable = new OffsetPageRequest(offset, limited);
        var postEntities = postRepository.findAllByUserIdAndStatusTrueOrderByCreatedDateDesc(signedInUser.getId(), pageable);

        var postResponses = postEntities.getContent().stream()
                .map(postMapper::toPostResponse)
                .peek(postResponse -> {
                    var favoriteEntities = favoriteRepository.findAllByPostId(postResponse.getId());
                    // Fetch users who liked the post
                    var userLikedPosts = favoriteEntities.stream()
                            .map(FavoriteEntity::getUser)
                            .map(userMapper::toUserResponse)
                            .collect(Collectors.toList());
                    postResponse.setUsersLikedPost(userLikedPosts);
                    // Set favorite type for the signed-in user
                    var favoriteEntityOpt = favoriteRepository.findByUserIdAndPostId(signedInUser.getId(), postResponse.getId());
                    var ratingType = favoriteEntityOpt
                            .map(FavoriteEntity::getType)
                            .map(type -> RatingType.valueOf(type.name()))
                            .orElse(RatingType.UNLIKE);
                    postResponse.setFavoriteType(ratingType);
                })
                .collect(Collectors.toList());

        logger.info("Post get succeeded with offset: {} and limited {}", postEntities.getNumber(), postEntities.getSize());
        return new PaginationPage<PostResponse>()
                .setRecords(postResponses)
                .setOffset(postEntities.getNumber())
                .setLimit(postEntities.getSize())
                .setTotalRecords(postEntities.getTotalElements());
    }

    @Transactional
    @Override
    public PostResponse updatePost(final UUID postId, final PostRequest postRequest) {
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

    }

    @Override
    public PaginationPage<PostResponse> getAllPostOrderByCreated(final Integer offset, final Integer limited) {
        var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
        var pageable = new OffsetPageRequest(offset, limited);
        var postEntities = postRepository.findAllByStatusTrueOrderByCreatedDateDesc(pageable);

        var postResponses = postEntities.getContent().stream()
                .map(postEntity -> {
                    var postResponse = postMapper.toPostResponse(postEntity);

                    // Fetch all favorites for this post
                    var favoriteEntities = favoriteRepository.findAllByPostId(postEntity.getId());

                    // Fetch users who liked the post
                    var userLikedPosts = favoriteEntities.stream()
                            .map(FavoriteEntity::getUser)
                            .map(userMapper::toUserResponse)
                            .toList();

                    postResponse.setUsersLikedPost(userLikedPosts);
                    // Set favorite type for the signed-in user
                    var favoriteEntityOpt = favoriteRepository.findByUserIdAndPostId(signedInUser.getId(), postEntity.getId());
                    var ratingType = favoriteEntityOpt
                            .map(FavoriteEntity::getType)
                            .map(type -> RatingType.valueOf(type.name()))
                            .orElse(RatingType.UNLIKE);
                    postResponse.setFavoriteType(ratingType);

                    return postResponse;
                })
                .collect(Collectors.toList());

        logger.info("Get feed list succeeded with offset: {} and limited {}", postEntities.getNumber(), postEntities.getSize());
        return new PaginationPage<PostResponse>()
                .setRecords(postResponses)
                .setOffset(postEntities.getNumber())
                .setLimit(postEntities.getSize())
                .setTotalRecords(postEntities.getTotalElements());
    }

    @Transactional
    @Override
    public void disablePost(final UUID postId) {
        var post = postRepository
                .findById(postId)
                .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));

        logger.info("Disabling post successfully by id {}", postId);
        post.setStatus(false);
        postRepository.save(post);
        // Disable all comments following this post
        var comments = commentRepository.findByPostId(postId);
        comments.stream()
                .filter(comment -> comment.getPost().getId().equals(postId))
                .forEach(comment -> {
                    comment.setStatus(false);
                    commentRepository.save(comment);
                });
        logger.info("Disabled post and associated comments successfully");
    }

    @Override
    public PostResponse getPostById(final UUID id) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
        logger.error("Get post successfully by id {} ", id);
        return postMapper.toPostResponse(post);

    }
}
