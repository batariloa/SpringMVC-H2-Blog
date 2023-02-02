package com.example.springmvch2blog.config;

import com.example.springmvch2blog.controller.AuthenticationController;
import com.example.springmvch2blog.service.AuthenticationService;
import com.example.springmvch2blog.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.springmvch2blog.config.CookieAuthenticationFilter.ACCESS_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenFilter.class);


    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.warn("Anonymous user." + SecurityContextHolder.getContext().getAuthentication());


        logger.warn("Running refresh token filter.");

        Optional<Cookie> refreshTokenCookie = Stream.of(Optional.ofNullable(request.getCookies())
                                                                .orElse(new Cookie[0]))
                                                    .filter(cookie-> CookieAuthenticationFilter.REFRESH_COOKIE_NAME.equals(cookie.getName()))
                                                    .findFirst();
        refreshTokenCookie.ifPresent(cookie -> {
            //if token is not valid
            logger.warn("Refresh token is present.");
            if(authenticationService.validateToken(cookie.getValue())){

                            //create new access token using refresh token
                    logger.warn("New access token getting created.");
                            Cookie newAccessTokenCookie = authenticationService.refreshAccessToken(cookie.getValue());
                            response.addCookie(newAccessTokenCookie);

                    userAuthenticationManager.authenticate(
                            new PreAuthenticatedAuthenticationToken(newAccessTokenCookie.getValue(), null)
                    );
                        }







        });
        filterChain.doFilter(request, response);

    }
}
