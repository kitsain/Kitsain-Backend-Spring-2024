package com.myblogbackend.blog.enums;

import com.myblogbackend.blog.config.security.oauth2.OAuth2GoogleUserInfo;
import com.myblogbackend.blog.config.security.oauth2.OAuth2UserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Getter
public enum OAuth2Provider {
    GOOGLE("google"),

    LOCAL("local"),

    GITHUB("github");

    private final String provider;

    private static final Map<String, OAuth2Provider> PROVIDERS;

    static {
        PROVIDERS = new HashMap<>();
        for (OAuth2Provider provider : OAuth2Provider.values()) {
            PROVIDERS.put(provider.getProvider(), provider);
        }
    }

    public static OAuth2Provider findByRegistrationId(final String registrationId) {
        return PROVIDERS.get(registrationId);
    }

    public static OAuth2UserInfo getOAuth2UserInfo(final String registrationId, final Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(OAuth2Provider.GOOGLE.getProvider())) {
            return new OAuth2GoogleUserInfo(attributes);
        }
        throw new OAuth2AuthenticationException(String.format("Sign-in with %s is not supported yet.", registrationId));
    }

}