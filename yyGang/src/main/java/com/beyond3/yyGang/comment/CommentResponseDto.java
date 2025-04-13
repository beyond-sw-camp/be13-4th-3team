package com.beyond3.yyGang.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private Long userId;
    private Long boardId;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> childComments; // 대댓글 리스트

//    // 명시적 생성자 추가
//    public CommentResponseDto(Long id, String content, Long userId, Long boardId, Long parentId, List<CommentResponseDto> childComments) {
//        this.id = id;
//        this.content = content;
//        this.userId = userId;
//        this.boardId = boardId;
//        this.parentId = parentId;
//        this.childComments = childComments;
//    }
    // 엔티티를 DTO로 변환하는 메서드
//    public static CommentResponseDto fromEntity(Comment comment, List<Comment> childComments) {
//        return new CommentResponseDto(
//                comment.getId(),
//                comment.getContent(),
//                comment.getUser().getUserId(),
//                comment.getBoard().getId(),
//                comment.getCreatedAt(),
//                comment.getModifiedAt(),
//                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
//                childComments.stream().map(c -> fromEntity(c, List.of())).toList() // 대댓글을 재귀적으로 변환
//        );
//    }

    public static CommentResponseDto fromEntity(Comment comment, List<Comment> childComments) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUserId(),
                comment.getBoard().getId(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                childComments.stream()
                        .map(c -> fromEntity(c, c.getChildComments())) // 자식 댓글을 실제로 넘겨서 재귀적으로 변환
                        .toList() // 자식 댓글들을 DTO로 변환
        );
    }
}
