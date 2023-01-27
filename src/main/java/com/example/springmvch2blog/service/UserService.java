package com.example.springmvch2blog.service;


import com.example.springmvch2blog.entity.User;
import com.example.springmvch2blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Optional<User> loadByUsername(String username){

        return userRepository.findByUsername(username);

    }
}
