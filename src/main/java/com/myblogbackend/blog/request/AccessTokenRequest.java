package com.myblogbackend.blog.request;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenRequest {

    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;
}