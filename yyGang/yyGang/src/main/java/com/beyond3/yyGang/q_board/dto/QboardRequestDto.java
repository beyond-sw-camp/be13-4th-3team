package com.beyond3.yyGang.q_board.dto;

import com.beyond3.yyGang.q_board.QuestionBoard;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QboardRequestDto {

    @NotBlank(message = "제목은 필수로 입력해야 합니다.")
    private String qboardTitle;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String qboardContent;

    public QuestionBoard toEntity(User user) {

        return QuestionBoard.builder()
                .qboardTitle(this.qboardTitle)
                .qboardContent(this.qboardContent)
                .user(user)
                .build();
    }

}

