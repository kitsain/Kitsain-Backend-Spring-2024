package com.myblogbackend.blog.request;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String description;
    private String price;
    private List<String> images;
    private Date expringDate;
}
