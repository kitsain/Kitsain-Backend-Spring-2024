package com.myblogbackend.blog.config.security.oauth2;

import java.util.Map;

public class OAuth2GoogleUserInfo extends OAuth2UserInfo {

    public OAuth2GoogleUserInfo(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(getAttributes().get("sub"));
    }

    @Override
    public String getName() {
        return String.valueOf(getAttributes().get("name"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(getAttributes().get("email"));
    }

    @Override
    public String getImageUrl() {
        return String.valueOf(getAttributes().get("picture"));
    }

    @Override
    public String getLocale() {
        return String.valueOf(getAttributes().get("locale"));
    }
}
