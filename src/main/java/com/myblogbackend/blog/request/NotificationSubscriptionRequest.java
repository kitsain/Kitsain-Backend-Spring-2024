package com.myblogbackend.blog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationSubscriptionRequest {
    @NotBlank
    private String deviceToken;
    @NotBlank
    private String topicName;
}
