package com.myblogbackend.blog.services;

import com.myblogbackend.blog.response.JwtResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IdTokenService {
    JwtResponse verifyIdToken(String token) throws GeneralSecurityException, IOException;
}
