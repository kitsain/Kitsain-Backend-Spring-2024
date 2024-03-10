package com.myblogbackend.blog.config.security.oauth2;


import com.myblogbackend.blog.config.security.UserPrincipal;
import com.myblogbackend.blog.enums.OAuth2Provider;
import com.myblogbackend.blog.models.UserEntity;
import com.myblogbackend.blog.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository userRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(oAuth2UserRequest);
        return proceedOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User proceedOAuth2User(final OAuth2UserRequest oAuth2UserRequest, final OAuth2User oAuth2User) {
        var provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        var oAuth2UserInfo =
                OAuth2Provider.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            log.info("Not found email {} from {} provider", provider, oAuth2UserInfo.getEmail());
            throw new OAuth2AuthenticationException("NOT_FOUND_IN_" + provider);
        }

        var userFound = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserEntity user;
        if (userFound.isPresent()) {
            user = userFound.get();
            log.info("[SOCIAL-SIGN-IN] Found email {} from {} provider in database", user.getEmail(), user.getProvider());
            if (user.getProvider() != OAuth2Provider.findByRegistrationId(provider)) {
                log.info("[SOCIAL-SIGN-IN] Signed up email {} in {} provider before", user.getEmail(), user.getProvider());
                throw new OAuth2AuthenticationException("SIGNED_UP_IN_" + user.getProvider());
            }
        } else {
            log.info("[SOCIAL-SIGN-IN] Register email {} from {} provider in database", provider, oAuth2UserInfo.getEmail());
            user = signUpNewAccount(oAuth2UserRequest, oAuth2UserInfo);

        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private UserEntity signUpNewAccount(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {
        var user = new UserEntity();
        user.setProvider(OAuth2Provider.findByRegistrationId(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setName(oAuth2UserInfo.getName());
        user.setActive(false);
        user.setIsPending(false);
        return userRepository.save(user);
    }
}
