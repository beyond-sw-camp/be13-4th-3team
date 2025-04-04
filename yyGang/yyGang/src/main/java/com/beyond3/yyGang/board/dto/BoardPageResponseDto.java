package com.beyond3.yyGang.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardPageResponseDto {
    private List<BoardResponseDto> content; // 게시글 목록
    private long totalElements; // 전체 게시글 개수


}
