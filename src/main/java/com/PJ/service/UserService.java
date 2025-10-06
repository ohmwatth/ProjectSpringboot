package com.PJ.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.PJ.repository.UserRepository;
import com.PJ.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // register user
    public User registerUser(User u) {
        // กำหนด role ถ้าไม่มี
        if (u.getRole() == null || u.getRole().isEmpty()) {
            u.setRole("STUDENT");
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepo.save(u);
    }

    // อัปเดต profile
    public User updateUser(User u) {
        return userRepo.save(u);
    }

    // ค้นหา user
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}