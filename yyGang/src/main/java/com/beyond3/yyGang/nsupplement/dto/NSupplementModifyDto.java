package com.beyond3.yyGang.nsupplement.dto;

import com.beyond3.yyGang.hfunction.HFunctionName;
import com.beyond3.yyGang.ingredient.domain.IngredientName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NSupplementModifyDto {

    private String productName; // 상품 이름

    private String caution; // 주의 사항

    private String brand;  // 브랜드

    private int price;  // 상품 가격

    private int stockQuantity;  // 재고

    private String productImage;

    private List<IngredientName> ingredients;   // 영양 성분

    private List<HFunctionName> hFunctionalItems;   // 건강 기능 정보

}
