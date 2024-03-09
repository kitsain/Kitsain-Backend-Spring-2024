package com.myblogbackend.blog.category;

import com.myblogbackend.blog.response.CategoryResponse;

import java.util.UUID;

public final class CategoryTestApi {

    public static CategoryEntity makeCategoryForSaving(final UUID id) {
        return CategoryEntity.builder()
                .id(id)
                .name("Category A")
                .build();
    }

    public static CategoryRequest prepareCategoryForRequesting() {
        return CategoryRequest.builder()
                .name("Category A")
                .build();
    }

    public static CategoryResponse toCategoryResponse(final CategoryEntity categoryEntity) {
        return CategoryResponse.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }

    public static CategoryRequest prepareCategoryForRequestingUpdate(final String name) {
        return CategoryRequest.builder()
                .name(name)
                .build();
    }

}
