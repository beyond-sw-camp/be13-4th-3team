package com.beyond3.yyGang.order.dto;

import com.beyond3.yyGang.order.OrderOption;
import com.beyond3.yyGang.order.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResultDto {

    private Long orderId;   // 주문 번호    // orderOptionId로 할까?

    private OrderStatus status;     // 주문 상태 -> 기본적으로 Pending 상태 -> 결제 후 Ordered로 변경

    private int totalPrice;     // 전체 주문 가격

    private LocalDateTime orderDate;    // 주문 날짜

    private List<OrderOptionDto> orderOptionDtoList;
}
