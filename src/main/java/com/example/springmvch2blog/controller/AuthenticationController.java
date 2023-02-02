package com.example.springmvch2blog.controller;


import com.example.springmvch2blog.config.CookieAuthenticationFilter;
import com.example.springmvch2blog.config.RefreshTokenFilter;
import com.example.springmvch2blog.dto.RegisterUserDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.User;
import com.example.springmvch2blog.service.UserService;
import com.example.springmvch2blog.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserDto> signIn(
            @AuthenticationPrincipal UserDto user,
            HttpServletResponse servletResponse
    ) {


        logger.warn("So principal is" + user);

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.ACCESS_COOKIE_NAME, jwtUtil.generateToken(user));
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge(5);
        authCookie.setPath("/");

        String refreshToken = jwtUtil.generateToken(user);
        User userDb = userService.loadByUsername(user.getUsername());
        userDb.setRefreshToken(refreshToken);


        Cookie refreshCookie = new Cookie(RefreshTokenFilter.REFRESH_COOKIE_NAME, refreshToken);
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
