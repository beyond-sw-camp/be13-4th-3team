package com.beyond3.yyGang.nutrientAnswer.dto;

import com.beyond3.yyGang.nutrientAnswer.NAnswer;
import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurtientAnswerRequestDto {

//    private Long questionId;    // 이건 PathVariable로 받으니까 중복 같은데
//
//    private Long userId;        // 사용자 Id도 입력해야 할까?

    @NotBlank(message = "답변 내용 작성은 필수입니다.")
    private String answerContent;

    public NAnswer toEntity(User user, NQuestion question) {
        return NAnswer.builder()
                .question(question)
                .aContent(answerContent)
                .user(user)
                .build();
    }

}
