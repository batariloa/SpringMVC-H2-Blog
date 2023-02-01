package com.example.springmvch2blog.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPostDto {

    private Long Id;
    private String text;
    private String title;
    private Long authorId;
    private Long ownerId;
}
