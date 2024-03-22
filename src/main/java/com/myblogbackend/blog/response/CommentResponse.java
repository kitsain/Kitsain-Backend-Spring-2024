package com.myblogbackend.blog.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private String content;
    private String userName;
}
