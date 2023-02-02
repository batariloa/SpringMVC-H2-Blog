package com.example.springmvch2blog.controller;


import com.example.springmvch2blog.config.CookieAuthenticationFilter;
import com.example.springmvch2blog.config.UsernamePasswordFilter;
import com.example.springmvch2blog.dto.RegisterUserDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.User;
import com.example.springmvch2blog.service.AuthenticationService;
import com.example.springmvch2blog.service.UserService;
import com.example.springmvch2blog.util.JwtUtil;
import com.example.springmvch2blog.util.UserDtoUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user
            ,
                                          HttpServletResponse servletResponse) {


        logger.warn("So principal is" + user);

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.ACCESS_COOKIE_NAME, jwtUtil.generateToken(user) );
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge(5);
        authCookie.setPath("/");

        String refreshToken = jwtUtil.generateToken(user);
        User userDb = userService.loadByUsername(user.username());
        userDb.setRefreshToken(refreshToken);


        Cookie refreshCookie = new Cookie(CookieAuthenticationFilter.REFRESH_COOKIE_NAME, refreshToken );
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setMaxAge(10000000);
        refreshCookie.setPath("/");



        servletResponse.addCookie(authCookie);


        servletResponse.addCookie(refreshCookie);

        return ResponseEntity.ok(user);
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@RequestBody RegisterUserDto user) {

        return ResponseEntity.ok(userService.registerUser(user));
    }



}
