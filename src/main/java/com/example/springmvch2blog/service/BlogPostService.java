package com.example.springmvch2blog.service;

import com.example.springmvch2blog.dto.BlogPostDto;
import com.example.springmvch2blog.dto.CreatePostRequest;
import com.example.springmvch2blog.dto.PostsResponse;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.BlogPost;
import com.example.springmvch2blog.repository.BlogPostRepository;
import com.example.springmvch2blog.util.BlogPostDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    public PostsResponse getBlogPostByOwnerId(Long id){


       Optional<List<BlogPost>> posts = blogPostRepository.findBlogPostByOwnerId(id);

        return posts.map(blogPosts -> new PostsResponse(blogPosts.stream()
                                                                 .map(BlogPostDtoUtil::toDto)
                                                                 .collect(Collectors.toList())))
                    .orElseGet(() -> new PostsResponse(new ArrayList<>()));

    }

    public BlogPostDto createPostForUser(CreatePostRequest post, UserDto userDto){

        return BlogPostDtoUtil.toDto(
                blogPostRepository.save(BlogPost.builder()
                        .ownerId(userDto.id())
                        .authorId(userDto.id())
                        .text(post.getText())
                        .title(post.getTitle())
                        .build()
                ));

    }
}
