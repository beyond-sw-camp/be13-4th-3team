package com.beyond3.yyGang.board.dto;

import com.beyond3.yyGang.board.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto{

    private Long id;

    private String title;

    private String content;

    private Long userid;

    private String userName;
    private String userEmail;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getBoardContent();
        this.userEmail = board.getUser().getEmail();
        this.userName = board.getUser().getName();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }



}
