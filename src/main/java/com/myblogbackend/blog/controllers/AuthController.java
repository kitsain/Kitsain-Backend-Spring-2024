package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.controllers.route.AuthRoutes;
import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.request.AccessTokenRequest;
import com.myblogbackend.blog.request.LoginFormRequest;
import com.myblogbackend.blog.request.SignUpFormRequest;
import com.myblogbackend.blog.request.TokenRefreshRequest;
import com.myblogbackend.blog.response.ApiResponse;
import com.myblogbackend.blog.services.AuthService;
import com.myblogbackend.blog.services.IdTokenService;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Objects;

@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + AuthRoutes.BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final IdTokenService tokenService;

    @Hidden
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(final @Valid @RequestBody LoginFormRequest loginRequest) {
        var jwtResponse = authService.userLogin(loginRequest);
        if (Objects.isNull(jwtResponse)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has been deactivated/locked !!"));
        }
        return ResponseEntity.ok(jwtResponse);
    }

    @Hidden
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(final @Valid @RequestBody SignUpFormRequest signUpRequest) throws TemplateException, IOException {
        var newUser = authService.registerUser(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    @Hidden
    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(final @RequestParam("token") String token) throws IOException {
        return authService.confirmationEmail(token);
    }

    @Hidden
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(final @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        var jwtResponse = authService.refreshJwtToken(tokenRefreshRequest);
        return ResponseEntity.ok().body(jwtResponse);
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<?> verifyTokenFromGoogle(final @Valid @RequestBody AccessTokenRequest accessTokenRequest)
            throws GeneralSecurityException, IOException {
        var jwtResponse = tokenService.verifyIdToken(accessTokenRequest.getAccessToken());
        return ResponseEntity.ok().body(jwtResponse);
    }
}