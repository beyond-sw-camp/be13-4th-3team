package com.beyond3.yyGang.review.dto;


import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.review.domain.Review;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 리뷰 작성 Dto
public class ReviewRequestDto {

    @NotBlank(message = "리뷰 내용은 필수로 작성해야 합니다.")
    private String content; // 리뷰 내용

    // 리뷰 사진 Url도 있으면 좋으려나?

    public Review toEntity(User user, NSupplement nSupplement) {
        return Review.builder()
                .user(user)
                .nSupplement(nSupplement)
                .content(content)
                .build();
    }

}
