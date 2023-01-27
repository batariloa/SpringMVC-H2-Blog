package com.example.springmvch2blog;


import com.example.springmvch2blog.config.CookieAuthenticationFilter;
import com.example.springmvch2blog.dto.RegisterUserDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.service.AuthenticationService;
import com.example.springmvch2blog.service.UserService;
import com.example.springmvch2blog.util.JwtUtil;
import com.example.springmvch2blog.util.UserDtoUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user,
                                          HttpServletResponse servletResponse) {

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.ACCESS_COOKIE_NAME, jwtUtil.generateToken(user) );
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");

        Cookie refreshCookie = new Cookie(CookieAuthenticationFilter.REFRESH_COOKIE_NAME, jwtUtil.generateToken(user) );
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");



        servletResponse.addCookie(authCookie);
        servletResponse.addCookie(refreshCookie);

        return ResponseEntity.ok(user);
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@RequestBody RegisterUserDto user) {

        return ResponseEntity.ok(userService.registerUser(user));
    }

}
