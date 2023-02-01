package com.example.springmvch2blog.controller;


import com.example.springmvch2blog.dto.BlogPostDto;
import com.example.springmvch2blog.dto.CreatePostRequest;
import com.example.springmvch2blog.dto.PostsResponse;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class BlogPostController {


    private final BlogPostService blogPostService;

    @PostMapping
    public BlogPostDto createNewPost(@AuthenticationPrincipal UserDto user, @RequestBody CreatePostRequest createPostRequest){


        return blogPostService.createPostForUser(createPostRequest, user);
    }
    @GetMapping
    public PostsResponse getLoggedInUsersPosts(@AuthenticationPrincipal UserDto user){

        Long userId = user.id();

        return blogPostService.getBlogPostByOwnerId(userId);

    }
}
