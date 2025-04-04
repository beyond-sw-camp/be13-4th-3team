package com.beyond3.yyGang.nutrientAnswer.dto;

import com.beyond3.yyGang.nutrientAnswer.NAnswer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NutrientAnswerResponseDto {

    // final 왜 넣었지?

//    private Long answerId;    -> 대답 아이디 필요한가

    private Long questionId;

    private String userId;  // 답글 작성자 -> 이거도 필요 없지 않나 그냥 판매자로 다 통일 아닌가

    private String answerContent;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public NutrientAnswerResponseDto(NAnswer nAnswer) {

//        this.answerId = nAnswer.getAnswerId();

        this.questionId = (nAnswer.getUser() != null) ? nAnswer.getUser().getUserId() : null;

        String email = nAnswer.getUser().getEmail();
        this.userId = email.substring(0, 2) + "*".repeat(email.indexOf("@") - 2) + email.substring(email.indexOf("@"));

        this.createdAt = nAnswer.getCreatedAt();

        this.modifiedAt = nAnswer.getModifiedAt();

        this.answerContent = nAnswer.getAContent();
    }

}
