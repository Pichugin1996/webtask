package com.dimastik.webtask.service;

import com.dimastik.webtask.exceptions.UserNotFoundException;
import com.dimastik.webtask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        com.dimastik.webtask.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        log.info("IN loadUserByUsername: " + user.toString());
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        return userDetails;
    }
}
