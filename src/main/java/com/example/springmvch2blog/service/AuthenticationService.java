package com.example.springmvch2blog.service;

import com.example.springmvch2blog.config.UsernamePasswordFilter;
import com.example.springmvch2blog.dto.CredentialsDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.User;
import com.example.springmvch2blog.util.JwtUtil;
import com.example.springmvch2blog.util.UserDtoUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.springmvch2blog.config.CookieAuthenticationFilter.ACCESS_COOKIE_NAME;


@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


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

    public User findByToken(String token){

        String username = jwtUtil.getUsernameFromToken(token);
        return userService.loadByUsername(username);
    }

    public Cookie refreshAccessToken(String refreshToken){

        Date expiration= jwtUtil.getExpirationDateFromToken(refreshToken);
        logger.warn("EXPIRATION IN REFRESH TOKEN"+ expiration.getTime());
        String newAccessToken = jwtUtil.generateToken(UserDtoUtil.toDto(findByToken(refreshToken)));
        //overwrite the old access token
        Cookie newAccessCookie = new Cookie(ACCESS_COOKIE_NAME, newAccessToken);
        newAccessCookie.setPath("/");
        newAccessCookie.setHttpOnly(true);
        newAccessCookie.setMaxAge(5000000);

        return newAccessCookie;
    }
    public boolean validateToken(String token){
        return jwtUtil.validateToken(token);
    }
}
