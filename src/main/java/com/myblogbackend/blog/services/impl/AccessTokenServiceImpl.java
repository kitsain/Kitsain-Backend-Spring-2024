package com.myblogbackend.blog.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.myblogbackend.blog.config.security.JwtProvider;
import com.myblogbackend.blog.enums.OAuth2Provider;
import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.models.UserEntity;
import com.myblogbackend.blog.repositories.UsersRepository;
import com.myblogbackend.blog.response.JwtResponse;
import com.myblogbackend.blog.services.IdTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

import static com.myblogbackend.blog.config.security.JwtProvider.ISSUER_GENERATE_TOKEN;
import static com.myblogbackend.blog.config.security.JwtProvider.SIGNING_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenServiceImpl implements IdTokenService {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String googleWebClientId = "709026956129-nt0ged8nsm2hq70ha2n4sne6j2rcplsr.apps.googleusercontent.com";
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final UsersRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public JwtResponse verifyIdToken(final String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(googleWebClientId))
                .build();
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            UserEntity user = userRepository.findByEmail(email)
                    .orElseGet(() -> createNewUser(email, name));
            String jwtToken = generateJwtToken(user);
            return new JwtResponse(jwtToken, "", jwtProvider.getExpiryDuration());
        } else {
            throw new BlogRuntimeException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }


    private String generateJwtToken(final UserEntity user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProvider.getExpiryDuration());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(ISSUER_GENERATE_TOKEN)
                .setId(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
    }

    private UserEntity createNewUser(final String email, final String name) {
        var user = new UserEntity();
        user.setProvider(OAuth2Provider.GOOGLE);
        user.setEmail(email);
        user.setName(name);
        user.setActive(false);
        user.setIsPending(false);
        userRepository.save(user);
        return user;
    }
}
