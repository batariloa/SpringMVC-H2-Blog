package com.example.springmvch2blog.config;

import com.example.springmvch2blog.dto.CredentialsDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.service.AuthenticationService;
import com.example.springmvch2blog.util.UserDtoUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;


@RequiredArgsConstructor
@Component
public class UserAuthenticationManager implements AuthenticationManager {
    
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationManager.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDto userDto = null;

        //if passed authentication is username and password
        if(authentication instanceof  UsernamePasswordAuthenticationToken){

            userDto = authenticationService.authenticate(new CredentialsDto((String) authentication.getPrincipal(),
                    (String) authentication.getCredentials()));
            logger.warn("DTO " + userDto.toString());

        }
        //if passed authentication is a JWT Cookie
        else if(authentication instanceof PreAuthenticatedAuthenticationToken){

            //
            userDto = UserDtoUtil.toDto(authenticationService.findByToken((String) authentication.getPrincipal()));
        }

        if(userDto== null) {
            logger.warn("UserDto is null.");
            return null;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDto,
                null,
                Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authenticationToken;

    }


}
