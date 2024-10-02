package com.Spring.Login.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Spring.Login.Repo.UserRepository;
import com.Spring.Login.User.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Use PasswordEncoder here

    public void register(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(user -> verifyPassword(password, user.getPassword()));
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean verifyPassword(String password, String hashed) {
        return passwordEncoder.matches(password, hashed);
    }
}
