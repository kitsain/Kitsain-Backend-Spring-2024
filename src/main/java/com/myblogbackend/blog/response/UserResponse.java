package com.myblogbackend.blog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;

}
