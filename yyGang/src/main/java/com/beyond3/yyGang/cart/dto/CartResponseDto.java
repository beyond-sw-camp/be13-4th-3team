package com.beyond3.yyGang.cart.dto;

import com.beyond3.yyGang.cart.domain.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    private Long cartId;

    private List<CartOptionDto> cartOptions;

    private int totalCount;

    public static CartResponseDto fromCart(Long cartId, List<CartOptionDto> cartOptions, int totalCount) {
        return CartResponseDto.builder()
                .cartId(cartId)
                .cartOptions(cartOptions)
                .totalCount(totalCount)
                .build();
    }
}