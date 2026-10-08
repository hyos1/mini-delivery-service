package com.myapp.delivery.domain.auth.controller;

import com.myapp.delivery.domain.auth.dto.request.LoginRequest;
import com.myapp.delivery.domain.auth.dto.request.SignupRequest;
import com.myapp.delivery.domain.auth.dto.response.LoginResponse;
import com.myapp.delivery.domain.auth.service.AuthService;
import com.myapp.delivery.domain.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    // 회원가입 성공 시 201
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse userResponse = userService.signup(request.getUsername(), request.getPassword(), request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    // 로그인 - Body에 jwt 반환
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        log.info("response: {}", response.getJwt());
        return ResponseEntity.ok(response);
    }
}