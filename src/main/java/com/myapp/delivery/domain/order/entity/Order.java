package com.myapp.delivery.domain.order.entity;

import com.myapp.delivery.domain.BaseEntity;
import com.myapp.delivery.domain.menu.entity.Menu;
import com.myapp.delivery.domain.order.enums.OrderState;
import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.global.exception.ClientException;
import com.myapp.delivery.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int totalAmount; // 총 가격
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState orderState;

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Order(int quantity, int totalAmount, Address address, User user, Menu menu) {
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.address = address;
        this.orderState = OrderState.ORDERED; // 처음 상태는 주문요청
        this.user = user;
        this.menu = menu;
    }

    // 총액은 요청으로 받지 않고 메뉴 가격 × 수량으로 직접 계산
    public static Order create(User user, Menu menu, int quantity, Address address) {
        validationQuantity(quantity);
        int totalAmount = menu.getPrice() * quantity;
        return new Order(quantity, totalAmount, address, user, menu);
    }

    // 엔티티 자체 검증 메서드

    public static void validationQuantity(int quantity) {
        if (quantity < 1) {
            throw new ClientException(ErrorCode.INVALID_ORDER_QUANTITY);
        }
    }
}
