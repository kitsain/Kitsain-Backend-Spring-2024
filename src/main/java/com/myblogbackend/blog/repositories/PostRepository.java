package com.myblogbackend.blog.repositories;

import com.myblogbackend.blog.models.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByCategoryId(Long categoryId);

}
