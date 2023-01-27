package com.example.springmvch2blog.config;

import com.example.springmvch2blog.dto.CredentialsDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.service.AuthenticationService;
import com.example.springmvch2blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;


@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDto userDto = null;

        logger.warn("PROVIDER USED");
        if(authentication instanceof  UsernamePasswordAuthenticationToken){

            logger.warn("IS INSTANCE OF USERNAMEPASSWORD");
            userDto = authenticationService.authenticate(new CredentialsDto((String) authentication.getPrincipal(),
                    (String) authentication.getCredentials()));

        }
        else if(authentication instanceof PreAuthenticatedAuthenticationToken){

            userDto = authenticationService.findByToken((String) authentication.getPrincipal());
        }


        if(userDto== null) {
            logger.warn("NOPE NULL");
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
