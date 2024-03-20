package com.myblogbackend.blog.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank(message = "Title info cannot be blank")
    private String title;
    private String description;
    @NotBlank(message = "Price info cannot be blank")
    private String price;
    @NotEmpty(message = "At least one image is required")
    private List<String> images;
    private Date expringDate;
}
