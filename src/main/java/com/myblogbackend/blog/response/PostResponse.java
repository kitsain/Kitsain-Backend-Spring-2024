package com.myblogbackend.blog.response;

import lombok.*;

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
    private UserResponse user;
}
