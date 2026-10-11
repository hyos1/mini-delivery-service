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

    // 주문 당시의 메뉴 데이터 - 스냅샷
    @Column(nullable = false)
    private String menuName; // 주문 당시 메뉴 이름
    @Column(nullable = false)
    private int menuPrice; // 주문 당시 메뉴 가격

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Order(int quantity, Address address, User user, Menu menu) {
        this.quantity = quantity;
        this.menuPrice = menu.getPrice();
        this.totalAmount = this.menuPrice * quantity; // 주문 당시 가격 * 수량으로 최종금액도 당시 최종금액
        this.address = address;
        this.orderState = OrderState.ORDERED; // 처음 상태는 주문요청
        this.user = user;
        this.menu = menu;
        this.menuName = menu.getName();
    }

    // 총액은 요청으로 받지 않고 메뉴 가격 × 수량으로 직접 계산
    public static Order create(User user, Menu menu, int quantity, Address address) {
        validationQuantity(quantity);
        validationAddress(address);
        // 메뉴 * 수량 = 최종 주문 가격(쿠폰이나 할인 없는 버전)
        return new Order(quantity, address, user, menu);
    }

    // 결제 완료 시 [주문요청 -> 결제완료]로 변경
    public void pay() {
        // "주문요청" 상태인 경우에만 "결제완료"로 변경 가능
        if (this.orderState != OrderState.ORDERED) {
            throw new ClientException(ErrorCode.INVALID_ORDER_STATE_FOR_PAY);
        }
        this.orderState = OrderState.PAID;
    }

    // 결제한 후 [결제완료 -> 주문 수락]으로 변경
    public void accept() {
        // "결제완료" 상태인 주문만 "주문수락" 가능
        if (this.orderState != OrderState.PAID) {
            throw new ClientException(ErrorCode.INVALID_ORDER_STATE_FOR_ACCEPT);
        }
        this.orderState = OrderState.ACCEPTED;
    }

    // 주문 수락 후 [주문수락 -> 배달완료]로 변경
    public void complete() {
        if (this.orderState != OrderState.ACCEPTED) {
            throw new ClientException(ErrorCode.INVALID_ORDER_STATE_FOR_DELIVERY_COMPLETE);
        }
        this.orderState = OrderState.COMPLETED;
    }

    // 주문 취소
    public void cancel() {
        // "주문요청" 상태인 경우에만 "주문취소"로 변경 가능
        if (this.orderState != OrderState.ORDERED) {
            throw new ClientException(ErrorCode.INVALID_ORDER_STATE_FOR_CANCEL);
        }
        this.orderState = OrderState.CANCELED;
    }

    // 엔티티 자체 검증 메서드

    public static void validationQuantity(int quantity) {
        if (quantity < 1) {
            throw new ClientException(ErrorCode.INVALID_ORDER_QUANTITY);
        }
    }

    public static void validationAddress(Address address) {
        if (address == null ||
                address.getCity() == null || address.getCity().isBlank() ||
                address.getStreet() == null || address.getStreet().isBlank() ||
                address.getZipCode() == null || address.getZipCode().isBlank()) {
            // ErrorCode는 프로젝트에 정의된 400 관련 에러코드를 사용하세요.
            throw new ClientException(ErrorCode.ADDRESS_REQUIRED);
        }
    }
}