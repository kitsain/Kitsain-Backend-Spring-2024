package com.myblogbackend.blog.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private String content;
    private String userName;
    private Date createdDate;
    private List<CommentResponse> replies = new ArrayList<>();

}
