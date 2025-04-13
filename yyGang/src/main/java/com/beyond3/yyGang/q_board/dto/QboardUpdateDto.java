package com.beyond3.yyGang.q_board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QboardUpdateDto {

    private String title;

    private String content;

    @Builder
    public QboardUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
