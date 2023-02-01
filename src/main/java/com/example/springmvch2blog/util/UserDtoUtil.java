package com.example.springmvch2blog.util;

import com.example.springmvch2blog.dto.RegisterUserDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.User;

public class UserDtoUtil {


    public static UserDto toDto(User user){

        return  UserDto.builder()
                .username(user.getUsername())
                .email(user.getUsername())
                .role(user.getRole())
                .password(user.getPassword())
                .id(user.getId())
                       .build();
    }


}
