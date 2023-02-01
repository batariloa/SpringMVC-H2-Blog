package com.example.springmvch2blog.repository;

import com.example.springmvch2blog.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {


    Optional<List<BlogPost>> findBlogPostByOwnerId(Long ownerId);


}
