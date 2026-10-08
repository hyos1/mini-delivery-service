package com.myapp.delivery.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderState {
    ORDERED("주문요청"),
    PAID("결제완료"),
    ACCEPTED("주문수락"),
    COMPLETED("배달완료"),
    CANCELED("주문취소");

    private final String description;

    OrderState(String description) {
        this.description = description;
    }
}
