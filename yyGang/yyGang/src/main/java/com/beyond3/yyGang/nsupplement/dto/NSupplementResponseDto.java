package com.beyond3.yyGang.nsupplement.dto;

import com.beyond3.yyGang.hfunction.HFunctionName;
import com.beyond3.yyGang.ingredient.domain.IngredientName;
import com.beyond3.yyGang.nsupplement.NSupplement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementResponseDto {

    private Long productId;  // 상품 Id

    private String productName; // 상품 이름

    private String caution; // 주의 사항

    private String brand;  // 브랜드

    private int price;  // 상품 가격

    private int stockQuantity;  // 재고

    private int reviewCount;    // 전체 리뷰 수

    private String productImage;

    private List<IngredientName> ingredients = new ArrayList<>();

    private List<HFunctionName> healthNames = new ArrayList<>();

    // NSupplement를 받아서 생성자 호출
    public NSupplementResponseDto(NSupplement supplement) {
        this.productId = supplement.getProductId();
        this.productName = supplement.getProductName();
        this.caution = supplement.getCaution();
        this.brand = supplement.getBrand();
        this.price = supplement.getPrice();
        this.stockQuantity = supplement.getStockQuantity();
        this.productImage = supplement.getProductImage();
    }

}
