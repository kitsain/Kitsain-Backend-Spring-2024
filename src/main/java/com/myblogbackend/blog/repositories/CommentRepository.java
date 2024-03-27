package com.myblogbackend.blog.repositories;

import com.myblogbackend.blog.models.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    Page<CommentEntity> findByPostIdAndPostStatusTrueOrderByCreatedDateDesc(UUID postId, Pageable pageable);

    List<CommentEntity> findByPostId(UUID postId);
}
