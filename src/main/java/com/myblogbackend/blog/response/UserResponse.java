package com.myblogbackend.blog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String name;

}
