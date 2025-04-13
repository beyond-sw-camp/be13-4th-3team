package com.beyond3.yyGang.pay.repository;

import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Order order);
}
