package com.example.springmvch2blog.dto;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
