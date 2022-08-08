package com.dimastik.webtask.service;

import com.dimastik.webtask.model.User;
import com.dimastik.webtask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String password) {
        User user = new User(username, passwordEncoder.encode(password), "USER", User.Status.ACTIVE);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public List<User> getAllUsers() {
        List<User> all = userRepository.findAll();

        Collections.sort(all, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return all;
    }

    public void setAdmin(Long id) {
        User user = userRepository.findById(id).get();
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public void setUser(Long id) {
        User user = userRepository.findById(id).get();
        user.setRole("USER");
        userRepository.save(user);
    }

    public void setNewPassword(String password, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
