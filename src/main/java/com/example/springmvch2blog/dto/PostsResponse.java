package com.example.springmvch2blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostsResponse {


    private List<BlogPostDto> posts;
}
