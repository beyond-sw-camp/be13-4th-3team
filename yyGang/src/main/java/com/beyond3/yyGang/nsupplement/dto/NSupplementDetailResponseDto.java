package com.beyond3.yyGang.nsupplement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementDetailResponseDto {

    @Schema(description = "제품ID")
    private Long productId;

    @Schema(description = "제품명")
    private String productName;

    @Schema(description = "주의사항")
    private String caution;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "가격")
    private int price;

    private int reviewCount;

    private String productImage;

}