package com.beyond3.yyGang.review.dto;

import com.beyond3.yyGang.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;  // 리뷰 ID

    private String userName; // 회원 이름

    private Long nSupplementId;  // 상품 ID

    private String content; // 리뷰 내용

    private LocalDateTime createdAt; // 작성 날짜 -> 수정 시 작성 날짜 변경되게

    private LocalDateTime modifiedAt;

    private int reviewCount;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.userName = review.getUser().getName();
        this.nSupplementId = review.getNSupplement().getProductId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
        this.reviewCount = review.getNSupplement().getReviewCount();
    }
}
