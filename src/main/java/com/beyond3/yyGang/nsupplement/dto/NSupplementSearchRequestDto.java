package com.beyond3.yyGang.nsupplement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementSearchRequestDto {

    private String productName;     // 상품 이름 검색

    private List<Long> healthIds = new ArrayList<>();   // 건강 기능 Id들 검색 -> set으로 하는건 어때?

    private List<Long> ingredientIds = new ArrayList<>();   // 건강 성분 Id 검색

    private String sortType;    // 정렬 타입 -> String 으로 받아서 넘기나?
}
