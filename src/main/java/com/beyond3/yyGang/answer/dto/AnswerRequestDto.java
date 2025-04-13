package com.beyond3.yyGang.answer.dto;


import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.q_board.QuestionBoard;
import com.beyond3.yyGang.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerRequestDto {

    private String answerContent;

    public Answer toEntity(QuestionBoard qBoard, User user) {
        return Answer.builder()
                .answerContent(answerContent)
                .user(user)
                .qboard(qBoard)
                .build();
    }
}
