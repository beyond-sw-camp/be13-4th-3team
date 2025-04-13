package com.beyond3.yyGang.cart.dto;

import com.beyond3.yyGang.cart.domain.CartOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOptionDto {

    private Long cartOptionId;

    private Long nSupplementId;

    private String nSupplementName;

    private String brand;

    private int quantity;

    private int StockQuantity;

    private int TotalPrice;

    private int ItemPrice;

    public static CartOptionDto fromCartOption(CartOption cartOption) {
        return CartOptionDto.builder()
                .cartOptionId(cartOption.getCartOptionID())
                .nSupplementId(cartOption.getNSupplement().getProductId())
                .nSupplementName(cartOption.getNSupplement().getProductName())
                .brand(cartOption.getNSupplement().getBrand())
                .quantity(cartOption.getQuantity())
                .StockQuantity(cartOption.getNSupplement().getStockQuantity())
                .TotalPrice(cartOption.getPrice())
                .ItemPrice(cartOption.getNSupplement().getPrice())
                .build();
    }

}
