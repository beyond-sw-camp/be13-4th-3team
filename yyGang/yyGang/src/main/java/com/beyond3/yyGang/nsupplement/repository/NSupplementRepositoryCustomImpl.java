package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDtoV2;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDtoV2;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.beyond3.yyGang.hfunction.QHFunctionalCategory.hFunctionalCategory;
import static com.beyond3.yyGang.hfunction.QHFunctionalItem.hFunctionalItem;
import static com.beyond3.yyGang.ingredient.domain.QIngredient.ingredient;
import static com.beyond3.yyGang.ingredient.domain.QIngredientCategory.ingredientCategory;
import static com.beyond3.yyGang.nsupplement.QNSupplement.nSupplement;

@Slf4j
public class NSupplementRepositoryCustomImpl implements NSupplementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NSupplementRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageResponseDto<NSupplementResponseDtoV2> searchPageV2(NSupplementSearchRequestDtoV2 searchRequest, Pageable pageable, SortType sortType) {

        // MySQL과 같은 DB에서는 orderBy 정렬 기준이 없으면 결과가 랜덤하게 나올 수 있음
        // 리뷰순 정렬이 들어가면 고쳐야 할듯
        log.info(searchRequest.toString());
        List<NSupplement> nSupplements = queryFactory
                .select(nSupplement)
                .from(nSupplement)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                )
                .orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("nsupplements: " + nSupplements.toString());
        // total 카운트 조회(전체 개수)
        Long totalCount = queryFactory
                .select(nSupplement.productId.count())
                .from(nSupplement)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                )
                .fetchOne();

        // 상품 Id : 건강기능 이름 튜플로 가져오기
        List<Tuple> hFuncCateTuple = queryFactory
                .select(
                        hFunctionalCategory.nSupplement.productId,
                        hFunctionalItem.healthName
                )
                .from(hFunctionalCategory)
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .where(hFunctionalCategory.nSupplement.in(nSupplements))
                .fetch();

        // 상품 ID : 영양 성분 튜플로 가져오기
        List<Tuple> ingrCateTuple = queryFactory
                .select(
                        ingredientCategory.nSupplement.productId,
                        ingredient.ingredientName
                )
                .from(ingredientCategory)
                .join(ingredientCategory.ingredient, ingredient)
                .where(ingredientCategory.nSupplement.in(nSupplements))
                .fetch();


        Map<Long, NSupplementResponseDtoV2> nSupplementMap = new LinkedHashMap<>();
        log.info("nSupplementMap: " + nSupplementMap.toString());
        List<NSupplementResponseDtoV2> nSupplementList = new ArrayList<>();

        // nSupplements를 nSupplementMap 타입으로 변환
        for (NSupplement nSupplement : nSupplements) {
            nSupplementMap.put(nSupplement.getProductId(),
                    new NSupplementResponseDtoV2(
                            nSupplement.getProductId(),
                            nSupplement.getProductName(),
                            nSupplement.getCaution(),
                            nSupplement.getBrand(),
                            nSupplement.getPrice(),
                            nSupplement.getProductImage()
                    ));
        }

        // hFuncCateTuple 정보를 NSupplementResponseDtoV2에 담기
        for (Tuple tuple : hFuncCateTuple) {
            NSupplementResponseDtoV2 dto = nSupplementMap.get(tuple.get(hFunctionalCategory.nSupplement.productId));
            if (dto != null) {
                dto.addHealthName(tuple.get(hFunctionalItem.healthName));
            }
        }

        for (Tuple tuple : ingrCateTuple) {
            NSupplementResponseDtoV2 dto = nSupplementMap.get(tuple.get(ingredientCategory.nSupplement.productId));
            if (dto != null) {
                dto.addIngredient(tuple.get(ingredient.ingredientName));
            }
        }

        List<NSupplementResponseDtoV2> content = new ArrayList<>(nSupplementMap.values());

        log.info(content.toString());
        Page<NSupplementResponseDtoV2> page = new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0);
        log.info("page: " + page.toString());
        return new PageResponseDto<>(page);
//        return null;
    }


    private BooleanExpression ingredientIdsEq(List<Long> ingredientIds) {

        // 성분 id 리스트가 비어있거나 null이면 null 반환
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return null;
        }

        // null을 제외한 성분 ID 리스트 생성
        List<Long> ingredientIdList = ingredientIds.stream().filter(Objects::nonNull).toList();
        log.info("ingredientIdList: " + ingredientIdList);

        return nSupplement.productId.in(
                queryFactory
                        .select(ingredientCategory.nSupplement.productId)
                        .from(ingredientCategory)
                        .where(ingredientCategory.ingredient.ingredientID.in(ingredientIdList))
                        .groupBy(ingredientCategory.nSupplement.productId)
                        .having(ingredientCategory.nSupplement.productId.count().eq(Long.valueOf(ingredientIdList.size()))) // 모든 성분을 포함하는 상품만 필터링
        );
    }



    // 해당 건강 기능을 가지고 있는지 확인
    private BooleanExpression healthIdsEq(List<Long> healthIds) {

        if (healthIds == null || healthIds.isEmpty()) {
            return null;
        }

        List<Long> healthIdList = healthIds.stream().filter(Objects::nonNull).toList();

        return nSupplement.productId.in(
                queryFactory
                        .select(hFunctionalCategory.nSupplement.productId)
                        .from(hFunctionalCategory)
                        .where(hFunctionalCategory.hFunctionalItem.healthId.in(healthIdList))
                        .groupBy(hFunctionalCategory.nSupplement.productId)
                        .having(hFunctionalCategory.nSupplement.productId.count().eq(Long.valueOf(healthIdList.size())))
        );
    }

    private BooleanExpression ingredientIdEq(Long ingredientID) {
        return ingredientID != null ? ingredientCategory.ingredient.ingredientID.eq(ingredientID) : null;
    }

    private BooleanExpression healthIdEq(Long healthId) {
        return healthId != null ? hFunctionalCategory.hFunctionalItem.healthId.eq(healthId) : null;
    }

    private BooleanExpression existsHealthIdEq(Long healthId) {

        // 값을 입력 받지 않으면 해당 상품이 조건 처리 없이 출력될 수 있도록 하기 위함
        if (healthId == null) {
            return null;
        }

        BooleanExpression result = nSupplement.productId.in(
                queryFactory
                        .select(hFunctionalCategory.nSupplement.productId)
                        .from(hFunctionalCategory)
                        .where(
                                hFunctionalCategory.hFunctionalItem.healthId.eq(healthId)
                        )
        );

        return result;
    }

    private BooleanExpression ingredientIDEqExists(Long ingredientID) {

        if (ingredientID == null) {
            return null;
        }

        BooleanExpression result = nSupplement.productId.in(
                queryFactory
                        .select(ingredientCategory.nSupplement.productId)
                        .from(ingredientCategory)
                        .where(
                                ingredientCategory.ingredient.ingredientID.eq(ingredientID)
                        )
        );

        return result;
    }


    // 상품 이름이 존재하는지 검색
    private BooleanExpression productNameContains(String productName) {
        return StringUtils.hasText(productName) ? nSupplement.productName.contains(productName) : null;
    }


}
