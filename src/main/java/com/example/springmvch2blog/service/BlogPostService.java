package com.example.springmvch2blog.service;

import com.example.springmvch2blog.dto.BlogPostDto;
import com.example.springmvch2blog.dto.CreatePostRequest;
import com.example.springmvch2blog.dto.PostsResponse;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.BlogPost;
import com.example.springmvch2blog.repository.BlogPostRepository;
import com.example.springmvch2blog.util.BlogPostDtoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private static final Logger logger = LoggerFactory.getLogger(BlogPostService.class);

    private final BlogPostRepository blogPostRepository;

    public PostsResponse getBlogPostByOwnerId(Long id) {


        Optional<List<BlogPost>> posts = blogPostRepository.findBlogPostByOwnerId(id);

        return posts.map(blogPosts -> new PostsResponse(blogPosts.stream()
                                                                 .map(BlogPostDtoUtil::toDto)
                                                                 .collect(Collectors.toList())))
                    .orElseGet(() -> new PostsResponse(new ArrayList<>()));

    }

    @Transactional
    public BlogPostDto createPostForUser(
            CreatePostRequest post,
            UserDto userDto
    ) {


        BlogPost postIs = BlogPost.builder()
                                  .ownerId(userDto.getId())
                                  .authorId(userDto.getId())
                                  .text(post.getText())
                                  .title(post.getTitle())
                                  .build();

        logger.warn("POST OBJECT:" + postIs.getText() + " but before that it was " + post.getText());


        return BlogPostDtoUtil.toDto(blogPostRepository.save(postIs));

    }
}
