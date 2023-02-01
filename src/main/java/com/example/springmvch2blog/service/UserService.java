package com.example.springmvch2blog.service;


import com.example.springmvch2blog.config.UsernamePasswordFilter;
import com.example.springmvch2blog.dto.RegisterUserDto;
import com.example.springmvch2blog.dto.UserDto;
import com.example.springmvch2blog.entity.User;
import com.example.springmvch2blog.repository.UserRepository;
import com.example.springmvch2blog.util.UserDtoUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordFilter.class);

    public UserDto loadByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {

            logger.warn("PRESENT IN DB" + email);

            return UserDtoUtil.toDto(user.get());
        } else {
            logger.warn("NOT PRESENT IN DB");

            throw new RuntimeException("User not found");
        }
    }

    public UserDto loadByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {

            return UserDtoUtil.toDto(user.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public UserDto registerUser(RegisterUserDto userDto){

        User user = User.builder()
                .password(bCryptPasswordEncoder.encode(userDto.password()))
                .email(userDto.email())
                .username(userDto.username())
                .role("ROLE_USER")
                .build();

        if(userRepository.existsByEmail(userDto.email())){

            throw new RuntimeException("Email is already taken");
        }

        if(userRepository.existsByUsername(userDto.username())){
            throw new RuntimeException("Username is already taken");
        }

       return UserDtoUtil.toDto(userRepository.save(user));


    }


}
