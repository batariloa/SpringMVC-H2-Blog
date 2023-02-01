package com.example.springmvch2blog.service;

import com.example.springmvch2blog.dto.CredentialsDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.util.JwtUtil;
import com.example.springmvch2blog.util.UserDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserService userService;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    public UserDto authenticate(CredentialsDto credentialsDto){

        UserDto user = userService.loadByEmail(credentialsDto.getEmail());


        if(passwordEncoder.matches(credentialsDto.getPassword(), user.password())){
            return user;
        }

        throw new RuntimeException("Invalid credentials");

    }

    public UserDto findByToken(String token){

        String username = jwtUtil.getUsernameFromToken(token);
        UserDto user = userService.loadByUsername(username);

        return user;
    }
}
