package com.beyond3.yyGang.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 부모 댓글(대댓글이 아닌 댓글)만 조회 (페이지네이션 지원)
    Page<Comment> findByBoardIdAndParentCommentIsNull(Long board, Pageable pageable);


    // 특정 댓글의 대댓글 조회 (페이징 없이 전체 조회)
    List<Comment> findByParentCommentId(Long parentId);
}
