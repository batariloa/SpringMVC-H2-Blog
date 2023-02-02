package com.example.springmvch2blog.config;

import com.example.springmvch2blog.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {
    public static final String ACCESS_COOKIE_NAME="accessToken";
    public static final String REFRESH_COOKIE_NAME="refreshToken";

    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //if accessToken is available
        Optional<Cookie> accessTokenCookie = Stream.of(Optional.ofNullable(request.getCookies())
                .orElse(new Cookie[0]))
                .filter(cookie-> ACCESS_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();


        boolean tokenCreated = accessTokenCookie.map(cookie -> {
                                                    if (authenticationService.validateToken(cookie.getValue())) {
                                                        userAuthenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(cookie.getValue(), null));
                                                        return true;
                                                    }
                                                    return false;
                                                }).filter(result -> result)
                                                .isPresent();

        //if token is created, do not call other authentication filters
        if (!tokenCreated) {
            filterChain.doFilter(request, response);
        }




}
    }
