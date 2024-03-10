package com.myblogbackend.blog.config.security.oauth2;

import java.util.Map;

public abstract class OAuth2UserInfo {

    private Map<String, Object> attributes;

    protected OAuth2UserInfo(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public abstract String getLocale();
}
