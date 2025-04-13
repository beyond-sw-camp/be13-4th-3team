package com.beyond3.yyGang.nutrientQuestion.dto;

import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import jakarta.annotation.security.DenyAll;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NutrientQuestionResponseDto {

    // 이거 final 로 한 이유가 있을까요?

    private Long questionId;  // 질문 Id 필요 할까

    private String userId;    // 작성자 Id -> 익명화

    private Long supplementId;    // 상품 Id는 있긴 해야지

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    private String qContent;

    @Builder
    public NutrientQuestionResponseDto(NQuestion nQuestion) {

        this.questionId = nQuestion.getQuestionId();

        String email = nQuestion.getUser().getEmail();
        this.userId = email.substring(0, 2) + "*".repeat(email.indexOf("@") - 2) + email.substring(email.indexOf("@"));

        this.supplementId = nQuestion.getSupplement().getProductId();

        this.createAt = nQuestion.getCreatedAt();

        this.modifiedAt = nQuestion.getModifiedAt();

        this.qContent = nQuestion.getQContent();
    }
}
