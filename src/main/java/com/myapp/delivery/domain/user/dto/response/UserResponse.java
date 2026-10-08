package com.myapp.delivery.domain.user.dto.response;

import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getCreatedAt(), user.getModifiedAt());
    }
}