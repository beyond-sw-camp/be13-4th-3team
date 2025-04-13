package com.beyond3.yyGang.board.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequestDto {

    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다")
    private String title;

    @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다")
    private String content;

}
