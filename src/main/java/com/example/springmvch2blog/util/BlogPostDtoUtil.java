package com.example.springmvch2blog.util;

import com.example.springmvch2blog.dto.BlogPostDto;
import com.example.springmvch2blog.entity.BlogPost;

public class BlogPostDtoUtil {


    public static BlogPostDto toDto(BlogPost blogPost){

        return BlogPostDto.builder()
                .Id(blogPost.getId())
                .title(blogPost.getTitle())
                .ownerId(blogPost.getOwnerId())
                .text(blogPost.getText())
                .authorId(blogPost.getAuthorId())
                .build();
    }
}
