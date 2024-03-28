package com.myblogbackend.blog.repositories;

import com.myblogbackend.blog.models.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface FavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {
    Optional<FavoriteEntity> findByUserIdAndPostId(UUID userId, UUID postId);

    FavoriteEntity findByPostId(UUID postId);

    List<FavoriteEntity> findAllByPostId(UUID postId);

}
