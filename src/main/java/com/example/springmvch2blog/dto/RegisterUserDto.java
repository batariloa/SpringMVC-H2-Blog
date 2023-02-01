package com.example.springmvch2blog.dto;

import lombok.Builder;

@Builder

public record RegisterUserDto (String username,
                               String password,
                               String email){}
