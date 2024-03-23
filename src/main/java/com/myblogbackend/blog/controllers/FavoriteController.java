package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.controllers.route.FavoriteRoutes;
import com.myblogbackend.blog.response.ResponseEntityBuilder;
import com.myblogbackend.blog.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + FavoriteRoutes.BASE_URL)
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PutMapping("/{postId}")
    public ResponseEntity<?> favourite(@PathVariable final UUID postId) {
        favoriteService.persistOrDelete(postId);
        return ResponseEntityBuilder
                .getBuilder()
                .build();
    }

}