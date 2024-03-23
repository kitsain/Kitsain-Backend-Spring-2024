package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.models.FavoriteEntity;
import com.myblogbackend.blog.models.UserEntity;
import com.myblogbackend.blog.repositories.FavoriteRepository;
import com.myblogbackend.blog.repositories.PostRepository;
import com.myblogbackend.blog.services.FavoriteService;
import com.myblogbackend.blog.utils.JWTSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private static final Logger logger = LogManager.getLogger(FavoriteServiceImpl.class);
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;

    @Override
    public void persistOrDelete(final UUID postId) {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var existingFavorite = favoriteRepository.findByUserIdAndPostId(signedInUser.getId(), postId);
            var post = postRepository.findById(postId)
                    .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));

            if (existingFavorite.isEmpty()) {
                // User did not favorite this post, so increment the favorite count and save the favorite
                post.setFavourite(post.getFavourite() + 1);
                favoriteRepository.save(FavoriteEntity.builder().user(UserEntity.builder().id(signedInUser.getId()).build())
                        .post(post).build());
                logger.info("User {} favorited post {} successfully", signedInUser.getId(), postId);
            } else {
                // User already favorited this post, so decrement the favorite count and delete the favorite
                post.setFavourite(Math.max(0, post.getFavourite() - 1));
                favoriteRepository.delete(existingFavorite.get());
                logger.info("User {} unfavorited post {} successfully", signedInUser.getId(), postId);
            }
            // Save the updated post with the new favorite count
            postRepository.save(post);
        } catch (Exception e) {
            logger.error("Failed to persist or delete favorite for post {}", postId, e);
            throw new RuntimeException("Failed to persist or delete favorite for post " + postId, e);
        }
    }
}
