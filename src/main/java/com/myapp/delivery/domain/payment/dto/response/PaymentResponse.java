package com.myapp.delivery.domain.payment.dto.response;

import com.myapp.delivery.domain.order.enums.OrderState;
import com.myapp.delivery.domain.payment.entity.Payment;
import com.myapp.delivery.domain.payment.enums.PayMethod;
import com.myapp.delivery.domain.payment.enums.PaymentState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private Long orderId;
    private int totalAmount;
    private PayMethod payMethod;
    private PaymentState paymentState;
    private OrderState orderState;
    private LocalDateTime createdAt;

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(), payment.getOrder().getId(), payment.getTotalAmount(),
                payment.getPayMethod(), payment.getStatus(), payment.getOrder().getOrderState() //이거 지연로딩 일어나지 않나
                , payment.getCreatedAt()
        );
    }
}