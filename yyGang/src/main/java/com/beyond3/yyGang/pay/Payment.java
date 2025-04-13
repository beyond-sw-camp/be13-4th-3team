package com.beyond3.yyGang.pay;

import com.beyond3.yyGang.EntityDate;
import com.beyond3.yyGang.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId; // 결제 ID

    private int totalPrice; // 결제 가격

    private String payMethod; // 결제 방법

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PayStatus payStatus;  // 결제 상태 - WAITING, FAIL, SUCCESS

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderId;  // 주문 아이디 참조

    @Builder
    public Payment(int totalPrice, String payMethod, Order order) {
        this.totalPrice = totalPrice;
        this.payMethod = payMethod;
        this.payStatus = PayStatus.WAITING;
        this.orderId = order;
    }

    public void setStatus(PayStatus status) {
        this.payStatus = status;
    }
}
