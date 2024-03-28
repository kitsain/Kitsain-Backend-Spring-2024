package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.enums.RatingType;
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
        var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
        var existingFavorite = favoriteRepository.findByUserIdAndPostId(signedInUser.getId(), postId);
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));

        existingFavorite.ifPresentOrElse(favorite -> {
            post.setFavourite(Math.max(0, post.getFavourite() - 1));
            favoriteRepository.delete(favorite);
            logger.info("User {} unfavorited post {} successfully", signedInUser.getId(), postId);
        }, () -> {
            post.setFavourite(post.getFavourite() + 1);
            favoriteRepository.save(
                    FavoriteEntity.builder()
                            .user(UserEntity.builder().id(signedInUser.getId()).build())
                            .type(RatingType.LIKE)
                            .post(post)
                            .build()
            );
            logger.info("User {} favorited post {} successfully", signedInUser.getId(), postId);
        });
        postRepository.save(post);
    }
}
