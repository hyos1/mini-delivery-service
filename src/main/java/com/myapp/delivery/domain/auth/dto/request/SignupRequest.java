package com.myapp.delivery.domain.auth.dto.request;

import com.myapp.delivery.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @Size(min = 4, max = 20, message = "아이디는 4~20자만 가능합니다.")
    @NotBlank(message = "빈 값은 안됩니다.")
    private String username; // 아이디
    @Size(min = 8, message = "비밀번호는 8자 이상 가능합니다.")
    private String password;
    @NotNull(message = "회원 역할은 필수입니다.")
    private UserRole role;
}