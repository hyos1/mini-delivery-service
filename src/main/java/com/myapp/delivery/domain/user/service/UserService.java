package com.myapp.delivery.domain.user.service;

import com.myapp.delivery.domain.user.dto.response.UserResponse;
import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.domain.user.repository.UserRepository;
import com.myapp.delivery.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원 전체 조회
    public List<UserResponse> findAll() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            userResponses.add(UserResponse.from(user));
        }
        return userResponses;
    }
}