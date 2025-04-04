package com.beyond3.yyGang.comment;

import com.beyond3.yyGang.board.Board;
import com.beyond3.yyGang.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class CommentRequestDto {

    private String content;
     private Long parentId;


    public Comment toEntity(User user, Board board, Comment parentComment){
        return Comment.builder()
                .content(this.content)
                .board(board)
                .user(user)
                .parentComment(parentComment)
                .build();
    }

}
