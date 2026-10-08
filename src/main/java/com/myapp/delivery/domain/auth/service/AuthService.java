package com.myapp.delivery.domain.auth.service;

import com.myapp.delivery.domain.auth.dto.response.LoginResponse;
import com.myapp.delivery.domain.user.dto.response.UserResponse;
import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.domain.user.enums.UserRole;
import com.myapp.delivery.domain.user.repository.UserRepository;
import com.myapp.delivery.global.exception.ClientException;
import com.myapp.delivery.global.exception.ErrorCode;
import com.myapp.delivery.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public UserResponse signup(String username, String rawPassword, UserRole role) {
        if (userRepository.existsByUsername(username)) {
            throw new ClientException(ErrorCode.DUPLICATE_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(username, encodedPassword, role);
        userRepository.save(user);

        return UserResponse.from(user);
    }

    // 로그인
    public LoginResponse login(String username, String rawPassword) {
        // 아이디, 비밀번호 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );
        // 비밀번호 확인
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ClientException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return new LoginResponse(jwtUtil.createToken(user.getId(), user.getRole().name()));
    }

}