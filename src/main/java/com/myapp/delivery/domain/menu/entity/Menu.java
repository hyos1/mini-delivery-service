package com.myapp.delivery.domain.menu.entity;

import com.myapp.delivery.domain.BaseEntity;
import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.global.exception.ClientException;
import com.myapp.delivery.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

// delete() 호출 시 실행될 UPDATE 쿼리
@SQLDelete(sql = "UPDATE menu SET is_deleted = true WHERE menu.menu_id = ?")
// 조회(select)할 때마다 is_deleted = false가 자동으로 붙어서 삭제된 데이터가 나오지 않는다.
@SQLRestriction("is_deleted = false")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    private String description;
    @Column(nullable = false)
    private boolean isDeleted = false; // soft delete 처리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Menu(User owner, String name, int price, String description) {
        this.user = owner;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static Menu create(User owner, String name, int price, String description) {
        validationName(name);
        validationPrice(price);
        return new Menu(owner, name, price, description);
    }

    public void update(String name, int price, String description) {

        validationName(name);
        validationPrice(price);

        this.name = name;
        this.price = price;

        if (description != null) {
            this.description = description;
        }
    }

    // 엔티티 자체 검증 메서드

    public static void validationName(String name) {
        // null이거나 비어있거나, 공백으로만 이루어 졌는지 확인
        if (name == null || name.isBlank()) {
            throw new ClientException(ErrorCode.INVALID_MENU_NAME);
        }
    }

    public static void validationPrice(int price) {
        if (price < 1) {
            throw new ClientException(ErrorCode.INVALID_MENU_PRICE);
        }
    }
}