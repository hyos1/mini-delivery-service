package com.myapp.delivery.domain.payment.entity;

import com.myapp.delivery.domain.BaseEntity;
import com.myapp.delivery.domain.order.entity.Order;
import com.myapp.delivery.domain.payment.enums.PayMethod;
import com.myapp.delivery.domain.payment.enums.PaymentState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Column(nullable = false)
    private int totalAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayMethod payMethod; // CARD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentState status; // PAID

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Payment(Order order, int totalAmount, PayMethod payMethod) {
        this.totalAmount = totalAmount;
        this.payMethod = payMethod;
        this.status = PaymentState.PAID; // PG 연동이 없으므로 저장 = 결제완료
        this.order = order;
    }

    // 결제 금액은 요청으로 받지 않고 주문 총액을 그대로 사용
    public static Payment create(Order order, PayMethod payMethod) {
        return new Payment(order, order.getTotalAmount(), payMethod);
    }
}
