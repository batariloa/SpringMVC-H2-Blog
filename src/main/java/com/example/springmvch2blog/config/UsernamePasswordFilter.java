package com.example.springmvch2blog.config;

import com.example.springmvch2blog.dto.CredentialsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class UsernamePasswordFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordFilter.class);

    private final UserAuthenticationManager userAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if("/auth/login".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())){

            CredentialsDto credentialsDto = MAPPER.readValue(request.getInputStream(), CredentialsDto.class);

            logger.warn("CREDS " + credentialsDto.getEmail() + " " +
                    credentialsDto.getPassword());


            userAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentialsDto.getEmail(),
                            credentialsDto.getPassword()
                    )

            );

        }
        filterChain.doFilter(request, response);

    }
}
