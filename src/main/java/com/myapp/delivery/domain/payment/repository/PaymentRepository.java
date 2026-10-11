package com.myapp.delivery.domain.payment.repository;

import com.myapp.delivery.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
