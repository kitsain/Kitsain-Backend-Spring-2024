package com.myblogbackend.blog.config.security.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Data
public class OAuth2Properties {

    private List<String> authorizedRedirectUris = new ArrayList<>();
}
