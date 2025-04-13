package com.beyond3.yyGang.cart.dto;

import com.beyond3.yyGang.order.dto.OrderOptionDto;
import lombok.Data;

import java.util.List;

@Data
public class CartRequestDto {

    private Long cartId;    // 장바구니 아이디

    private List<OrderOptionDto> items;  // 장바구니 항목들
}
