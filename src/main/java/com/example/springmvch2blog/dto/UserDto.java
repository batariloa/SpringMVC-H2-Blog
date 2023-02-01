package com.example.springmvch2blog.dto;


import lombok.Builder;
import lombok.ToString;


@Builder

public record UserDto(
        Long id,
        String username,
        String password,
        String email,
        String role){}
