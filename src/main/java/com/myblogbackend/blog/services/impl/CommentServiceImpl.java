package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.mapper.CommentMapper;
import com.myblogbackend.blog.models.CommentEntity;
import com.myblogbackend.blog.pagination.OffsetPageRequest;
import com.myblogbackend.blog.pagination.PaginationPage;
import com.myblogbackend.blog.repositories.CommentRepository;
import com.myblogbackend.blog.repositories.PostRepository;
import com.myblogbackend.blog.repositories.UsersRepository;
import com.myblogbackend.blog.request.CommentRequest;
import com.myblogbackend.blog.response.CommentResponse;
import com.myblogbackend.blog.services.CommentService;
import com.myblogbackend.blog.utils.JWTSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final UsersRepository usersRepository;
    private final CommentRepository commentRepository;
    private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);
    @Override
    public PaginationPage<CommentResponse> getListCommentsByPostId(final Integer offset,
                                                                   final Integer limited,
                                                                   final UUID postId) {
        try {
            var post = postRepository.findById(postId)
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
            var pageable = new OffsetPageRequest(offset, limited);
            Page<CommentEntity> comments = commentRepository.findByPostId(post.getId(), pageable);
            logger.info("Retrieved comments for post ID {}", postId);
            List<CommentResponse> commentResponses = commentMapper.toListCommentResponse(comments.getContent());
            return new PaginationPage<CommentResponse>()
                    .setRecords(commentResponses)
                    .setOffset(comments.getNumber())
                    .setLimit(comments.getSize())
                    .setTotalRecords(comments.getTotalElements());
        } catch (Exception e) {
            logger.error("Failed to retrieve comments for post ID {}", postId, e);
            throw new RuntimeException("Failed to retrieve comments for post ID " + postId);
        }
    }

    @Transactional
    @Override
    public CommentResponse createNewComment(final CommentRequest commentRequest) {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var post = postRepository.findById(commentRequest.getPostId())
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
            // If the comment has a parent comment, retrieve it from the database
            CommentEntity parentComment = null;
            if (commentRequest.getParentCommentId() != null) {
                parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                        .orElseThrow(() -> new BlogRuntimeException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
            }
            var commentEntity = commentMapper.toCommentEntity(commentRequest);
            var userFound = usersRepository.findById(signedInUser.getId()).orElseThrow();
            commentEntity.setUser(userFound);
            commentEntity.setPost(post);
            commentEntity.setParentComment(parentComment);
            commentEntity.setStatus(true);
            commentEntity.setCreatedBy(signedInUser.getName());
            var createdComment = commentRepository.save(commentEntity);
            logger.info("Created the comment for post ID {} by user ID {}",
                    commentRequest.getPostId(), signedInUser.getId());
            return commentMapper.toCommentResponse(createdComment);
        } catch (Exception e) {
            logger.error("Failed to create the comment", e);
            throw new RuntimeException("Failed to create the comment");
        }
    }

    @Transactional
    @Override
    public CommentResponse updateComment(final UUID commentId, final CommentRequest commentRequest) {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var existingComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.COMMENT_NOT_FOUND));
            if (!existingComment.getUser().getId().equals(signedInUser.getId())) {
                throw new BlogRuntimeException(ErrorCode.UNABLE_EDIT_COMMENT);
            }
            existingComment.setContent(commentRequest.getContent());
            var updatedComment = commentRepository.save(existingComment);
            logger.info("Updated the comment with ID {} by user ID {}",
                    commentId, signedInUser.getId());
            return commentMapper.toCommentResponse(updatedComment);
        } catch (Exception e) {
            logger.error("Failed to update the comment", e);
            throw new RuntimeException("Failed to update the comment");
        }
    }
}
