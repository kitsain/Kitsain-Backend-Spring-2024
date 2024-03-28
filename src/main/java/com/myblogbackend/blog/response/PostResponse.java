package com.myblogbackend.blog.response;

import com.myblogbackend.blog.enums.RatingType;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private UUID id;
    private String title;
    private String description;
    private List<String> images;
    private String price;
    private Date expringDate;
    private Long favourite = 0L;
    private RatingType favoriteType;
    private UserResponse user;
    private List<UserLikedPostResponse> usersLikedPost = new ArrayList<>();
    private String createdBy;
}
