package com.myblogbackend.blog.repositories;

import com.myblogbackend.blog.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    UserEntity findByPostsId(UUID id);
}