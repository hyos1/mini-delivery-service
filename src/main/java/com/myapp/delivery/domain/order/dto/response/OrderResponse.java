package com.myapp.delivery.domain.order.dto.response;

import com.myapp.delivery.domain.order.entity.Order;
import com.myapp.delivery.domain.order.enums.OrderState;
import com.myapp.delivery.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private Long id; // 주문 ID
    private String menuName; // 메뉴 이름
    private int quantity; // 주문 수량
    private int totalAmount; // 이 주문의 최종 가격
//    private UserRole userRole; // 요청한 회원의 자격
    private OrderState orderState; // 현재 주문 상태
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(), order.getMenuName(), order.getQuantity(),
                order.getTotalAmount(), order.getOrderState(),
                order.getCreatedAt(), order.getModifiedAt()
        );
    }
//    public static OrderResponse from(Order order, UserRole userRole) {
//        return new OrderResponse(
//                order.getId(), order.getMenuName(), order.getQuantity(),
//                order.getTotalAmount(), userRole, order.getOrderState(),
//                order.getCreatedAt(), order.getModifiedAt()
//        );
//    }
}