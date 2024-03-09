package com.myblogbackend.blog.fakers;

import com.myblogbackend.blog.repositories.PostRepository;
import com.myblogbackend.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class GeneratePostsCommand implements CommandLineRunner {

    private final PostService postService;
    private final Environment environment;
    private final PostRepository postRepository;


    @Override
    public void run(final String... args) throws Exception {
//        boolean shouldGenerate = environment.getProperty("app.generate-posts", Boolean.class, true);
//        if (!shouldGenerate) {
//            return;
//        }
//        Faker faker = new Faker();
//        var postEntityList = postRepository.findAll();
//        var category = categoryRepository.findAll().get(0);
//        if (postEntityList.size() < 5) {
//            for (int i = 0; i < 10; i++) {
//                PostRequest postRequest = PostRequest.builder()
//                        .title(faker.commerce().department())
//                        .content(faker.commerce().productName())
//                        .categoryId(category.getId())
//                        .build();
//                postService.createPost(postRequest);
//            }
//        }
    }
}