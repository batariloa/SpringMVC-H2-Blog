package com.example.springmvch2blog.controller;


import com.example.springmvch2blog.dto.BlogPostDto;
import com.example.springmvch2blog.dto.CreatePostRequest;
import com.example.springmvch2blog.dto.PostsResponse;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class BlogPostController {
    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);
    private final BlogPostService blogPostService;

    @PostMapping
    public BlogPostDto createNewPost(
            @AuthenticationPrincipal UserDto user,
            @RequestBody CreatePostRequest createPostRequest
    ) {


        logger.warn("Text" + createPostRequest.getText() + " title " + createPostRequest.getTitle());
        return blogPostService.createPostForUser(createPostRequest, user);
    }

    @GetMapping
    public PostsResponse getLoggedInUsersPosts(@AuthenticationPrincipal UserDto user) {

        Long userId = user.getId();

        return blogPostService.getBlogPostByOwnerId(userId);

    }
}
