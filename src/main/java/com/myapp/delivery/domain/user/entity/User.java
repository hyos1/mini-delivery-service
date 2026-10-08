package com.myapp.delivery.domain.user.entity;

import com.myapp.delivery.domain.BaseEntity;
import com.myapp.delivery.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String username; // 로그인 시 ID
    @Column(nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // 필요해질 때 양방향 설정
//    @OneToMany(mappedBy = "user")
//    private List<Menu> menus = new ArrayList<>();

    private User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User create(String username, String password, UserRole role) {
        return new User(username, password, role);
    }
}