package com.myblogbackend.blog.mapper;

import com.myblogbackend.blog.models.UserEntity;
import com.myblogbackend.blog.response.UserLikedPostResponse;
import com.myblogbackend.blog.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserDTO(UserEntity userEntity);

    UserLikedPostResponse toUserResponse(UserEntity userEntity);

}
