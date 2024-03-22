package com.myblogbackend.blog.mapper;

import com.myblogbackend.blog.models.CommentEntity;
import com.myblogbackend.blog.request.CommentRequest;
import com.myblogbackend.blog.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toCommentEntity(CommentRequest commentRequest);

    @Mapping(source = "commentEntity.user.name", target = "userName")
    CommentResponse toCommentResponse(CommentEntity commentEntity);

    @Mapping(source = "commentEntity.user.name", target = "userName")
    List<CommentResponse> toListCommentResponse(List<CommentEntity> commentEntityList);
}
