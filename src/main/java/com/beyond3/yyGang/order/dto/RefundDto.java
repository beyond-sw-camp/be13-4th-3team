package com.beyond3.yyGang.order.dto;

import com.beyond3.yyGang.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundDto {
    // 주문 취소 결과
    private Long orderId;

    private int refundAmount;

    private OrderStatus orderStatus;
}
