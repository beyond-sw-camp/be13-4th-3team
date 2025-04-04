package com.beyond3.yyGang.nsupplement.repository;


import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.beyond3.yyGang.nsupplement.QNSupplement.nSupplement;

// 정렬 순서 정의 ENUM 클래스
@Getter
@RequiredArgsConstructor
public enum SortType {

    PRICE_ASC(nSupplement.price.asc()),     // 가격 오름차순
    PRICE_DESC(nSupplement.price.desc()),   // 가격 내림차순
    /*REVIEW_ASC(),
    REVIEW_DESC(),*/
    NAME_ASC(nSupplement.productName.asc()),    // 이름 오름차순
    NAME_DESC(nSupplement.productName.desc()),  // 이름 내림차순
    REVIEW_ASC(nSupplement.reviewCount.asc()),      // 리뷰수 오름차순
    REVIEW_DESC(nSupplement.reviewCount.desc());    // 리뷰수 내림차순

    private final OrderSpecifier<?> orderSpecifier; // 쿼리 정렬을 동적으로 처리할 수 있도록 돕는 객체

    // sortTypeName -> 이걸 SortType ENUM으로 변경하는 메서드
    public static SortType requestSortType(String sortTypeName) {

        // ENUM에 없는 값을 가져오면 예외 처리
        // return EnumUtil.getEnumValue(SortType.class, sortTypeName);

        // String으로 들어온 값을 ENUM으로 바꿔주는 메소드 -> 없으면 단순 이름 정렬로
        return Arrays.stream(SortType.values())
                .filter(type -> type.name().equalsIgnoreCase(sortTypeName))
                .findFirst()
                .orElse(NAME_ASC);
    }
}
