package com.example.springmvch2blog.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {
    public static final String ACCESS_COOKIE_NAME="accessToken";
    public static final String REFRESH_COOKIE_NAME="refreshToken";

    private final UserAuthenticationManager userAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<Cookie> accessTokenCookie = Stream.of(Optional.ofNullable(request.getCookies())
                .orElse(new Cookie[0]))
                .filter(cookie-> ACCESS_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();


        accessTokenCookie.ifPresent(cookie ->
                userAuthenticationManager.authenticate(
                                new PreAuthenticatedAuthenticationToken(cookie.getValue(), null)
                        )
                );


        filterChain.doFilter(request, response);


}
    }
