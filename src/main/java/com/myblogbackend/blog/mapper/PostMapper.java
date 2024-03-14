package com.myblogbackend.blog.mapper;

import com.myblogbackend.blog.models.PostEntity;
import com.myblogbackend.blog.request.PostRequest;
import com.myblogbackend.blog.response.PostResponse;
import com.myblogbackend.blog.utils.GsonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    PostEntity toPostEntity(PostRequest postRequest);

    @Mapping(target = "images", source = "images", qualifiedByName = "mapImagesInformation")
    PostResponse toPostResponse(PostEntity postEntity);

    List<PostResponse> toListPostResponse(List<PostEntity> postEntityList);

    PostResponse toPostResponseFromObject(Object object);

    List<PostResponse> toListPostResponseFromObject(List<Object> objectList);

    @Named("mapImages")
    default String mapImages(List<String> s3Ids) {
        return GsonUtils.arrayToString(s3Ids);
    }

    @Named("mapImagesInformation")
    default List<String> mapImagesInformation(String images) {
        return GsonUtils.stringToArray(images, String[].class);
    }

}
