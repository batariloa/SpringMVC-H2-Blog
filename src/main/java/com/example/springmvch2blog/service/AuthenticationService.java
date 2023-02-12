package com.example.springmvch2blog.service;

import com.example.springmvch2blog.config.RefreshTokenFilter;
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

    public UserDto authenticate(CredentialsDto credentialsDto) {

        UserDto user = userService.loadByEmail(credentialsDto.getEmail());

        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid credentials");

    }

    public User findByToken(String token) {

        String username = jwtUtil.getUsernameFromToken(token);
        return userService.loadByUsername(username);
    }

    public Cookie refreshAccessTokenCookie(String refreshToken) {

        Date expiration = jwtUtil.getExpirationDateFromToken(refreshToken);
        logger.warn("EXPIRATION IN REFRESH TOKEN" + expiration.getTime());
        logger.warn("USER DTO OF NEW ACCESS TOKEN" + UserDtoUtil.toDto(findByToken(refreshToken))
                                                                .toString());


        return generateRefreshedAccessTokenCookie(refreshToken);
    }

    public Cookie generateRefreshedAccessTokenCookie(String refreshToken) {
        String newAccessToken = jwtUtil.generateAccessToken(UserDtoUtil.toDto(findByToken(refreshToken)));
        //overwrite the old access token
        Cookie newAccessCookie = new Cookie(ACCESS_COOKIE_NAME, newAccessToken);
        newAccessCookie.setPath("/");
        newAccessCookie.setSecure(false);

        newAccessCookie.setHttpOnly(true);

        return newAccessCookie;
    }


    public Cookie generateRefreshTokenCookie(UserDto user) {
        String refreshToken = jwtUtil.generateRefreshToken(user);
        Cookie newAccessCookie = new Cookie(RefreshTokenFilter.REFRESH_COOKIE_NAME, refreshToken);
        newAccessCookie.setPath("/");
        newAccessCookie.setHttpOnly(true);

        return newAccessCookie;
    }

    public Cookie generateAccesssTokenCookie(UserDto user) {
        String refreshToken = jwtUtil.generateAccessToken(user);
        Cookie newAccessCookie = new Cookie(ACCESS_COOKIE_NAME, refreshToken);
        newAccessCookie.setPath("/");
        newAccessCookie.setSecure(false);
        newAccessCookie.setMaxAge(50000);
        newAccessCookie.setHttpOnly(true);

        return newAccessCookie;
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
