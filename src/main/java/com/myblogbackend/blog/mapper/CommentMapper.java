package com.myblogbackend.blog.mapper;

import com.myblogbackend.blog.models.CommentEntity;
import com.myblogbackend.blog.request.CommentRequest;
import com.myblogbackend.blog.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toCommentEntity(CommentRequest commentRequest);

    @Mapping(source = "commentEntity.user.name", target = "userName")
    CommentResponse toCommentResponse(CommentEntity commentEntity);

    default List<CommentResponse> toListCommentResponse(List<CommentEntity> commentEntityList) {
        Map<UUID, CommentResponse> commentResponseMap = new HashMap<>();
        List<CommentResponse> rootComments = new ArrayList<>();

        // Map comment entities to comment response objects and populate the map
        for (CommentEntity commentEntity : commentEntityList) {
            CommentResponse commentResponse = toCommentResponse(commentEntity);
            commentResponseMap.put(commentResponse.getId(), commentResponse);
        }

        // Iterate through the comment entities again to build the nested structure
        for (CommentEntity commentEntity : commentEntityList) {
            CommentResponse commentResponse = commentResponseMap.get(commentEntity.getId());
            if (commentEntity.getParentComment() == null) {
                rootComments.add(commentResponse); // Add root comments
            } else {
                UUID parentId = commentEntity.getParentComment().getId();
                CommentResponse parentResponse = commentResponseMap.get(parentId);
                if (parentResponse != null) {
                    parentResponse.getReplies().add(commentResponse);
                }
            }
        }
        return rootComments;
    }
}
