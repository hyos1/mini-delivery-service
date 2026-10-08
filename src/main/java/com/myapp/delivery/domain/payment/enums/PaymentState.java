package com.myapp.delivery.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentState {
    PAID("결제완료"),
    CANCELED("결제취소");

    private final String description;

    PaymentState(String description) {
        this.description = description;
    }
}
