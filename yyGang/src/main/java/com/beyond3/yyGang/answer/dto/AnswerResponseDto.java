package com.beyond3.yyGang.answer.dto;

import com.beyond3.yyGang.answer.domain.Answer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerResponseDto {

    private Long answerId;  // 대답 id가 필요한가?

    private String answerContent;

    private LocalDateTime answerDate;

    private LocalDateTime answerMdate;

    private String userId;

    // 질문 Id 필요한가..?

    private Long questionId;   // 질문 제목 or Id

    @Builder
    public AnswerResponseDto(Answer answer) {

        this.answerId = answer.getAnswerId();

        this.answerContent = answer.getAnswerContent();

        this.answerDate = answer.getCreatedAt();

        this.answerMdate = answer.getModifiedAt();

        // 질문 작성자 email을 바탕으로 익명화 해서 응답함
        String email = answer.getUser().getEmail();
//        this.userId = email.substring(0, 2) + "*".repeat(email.indexOf("@") - 2) + email.substring(email.indexOf("@"));

        this.userId = answer.getUser().getName();

        this.questionId = answer.getQboard().getQboardId();
    }
}
