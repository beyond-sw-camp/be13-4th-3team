package com.beyond3.yyGang.q_board.dto;

import com.beyond3.yyGang.q_board.QuestionBoard;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QboardDto {
    private final Long qboardId;
    private final String qboardTitle;
    private final String qboardContent;
    private final LocalDateTime qboardDate;
    private final LocalDateTime qboardMdate;
    private final String username;

    public QboardDto(QuestionBoard questionBoard) {
        this.qboardId = questionBoard.getQboardId();
        this.qboardTitle = questionBoard.getQboardTitle();
        this.qboardContent = questionBoard.getQboardContent();
        this.qboardDate = questionBoard.getCreatedAt();
        this.qboardMdate = questionBoard.getModifiedAt();
        this.username = questionBoard.getUser().getName(); // Lazy 로딩 문제 방지
    }
}
