package com.beyond3.yyGang.q_board.dto;


import com.beyond3.yyGang.q_board.QuestionBoard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QboardResponseDto {

    private Long qboardId;  // 게시글 Id는 필요한가?

    private String qboardTitle;

    private String qboardContent;

    private LocalDateTime qboardDate;

    private LocalDateTime qboardMdate;

    private String userId;    // 사용자 아이디 말고 이메일 앞 쪽 내용을 보여주는 편이 안전할 것으로 판단
//    private List<AnswerResponseDto> answers;

    private int viewCount;

    @Builder
    public QboardResponseDto(QuestionBoard questionBoard) {
        this.qboardId = questionBoard.getQboardId();
        this.qboardTitle = questionBoard.getQboardTitle();

        this.qboardContent = questionBoard.getQboardContent();

        this.qboardDate = questionBoard.getCreatedAt();

        this.qboardMdate = questionBoard.getModifiedAt();

        this.viewCount = questionBoard.getViewCount();

        // 사용자 아이디 전략의 경우 -> 이메일 앞 2글자만 보이고 나머진 *로 변경
        String email = questionBoard.getUser().getEmail();
        this.userId = email.substring(0, 2) + "*".repeat(email.indexOf("@") - 2) + email.substring(email.indexOf("@"));
//        this.answers = questionBoard.getAnswers().stream().map(AnswerResponseDto::new).collect(Collectors.toList());
    }
}
