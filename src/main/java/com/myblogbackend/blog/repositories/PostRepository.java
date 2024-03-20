package com.myblogbackend.blog.repositories;

import com.myblogbackend.blog.models.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    Page<PostEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query("SELECT p FROM PostEntity p ORDER BY p.createdDate DESC")
    Page<PostEntity> findAllOrderByCreatedDateDesc(Pageable pageable);

}
