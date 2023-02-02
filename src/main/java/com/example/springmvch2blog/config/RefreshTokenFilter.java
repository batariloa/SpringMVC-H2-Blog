package com.example.springmvch2blog.config;

import com.example.springmvch2blog.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenFilter.class);

    public static final String REFRESH_COOKIE_NAME="refreshToken";

    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        logger.warn("Calling refresh token filter.");

        Optional<Cookie> refreshTokenCookie = Stream.of(Optional.ofNullable(request.getCookies())
                                                                .orElse(new Cookie[0]))
                                                    .filter(cookie ->
                                                            REFRESH_COOKIE_NAME.equals(cookie.getName()))
                                                    .findFirst();
        //if refresh token is present
        refreshTokenCookie.ifPresent(cookie -> {

            //if the token is valid
            if (authenticationService.validateToken(cookie.getValue())) {

                //create new access token using refresh token
                logger.warn("New access token getting created.");
                Cookie newAccessTokenCookie = authenticationService.refreshAccessTokenCookie(cookie.getValue());
                response.addCookie(newAccessTokenCookie);

                //authenticate in manager
                userAuthenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(newAccessTokenCookie.getValue(),
                        null));

            }
        });

        //call the rest of the filters
        filterChain.doFilter(request, response);

    }
}
