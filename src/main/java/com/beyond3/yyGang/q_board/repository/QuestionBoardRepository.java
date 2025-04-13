package com.beyond3.yyGang.q_board.repository;

import com.beyond3.yyGang.q_board.QuestionBoard;
import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@Transactional
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {

    // 기본적인 조회는 생성일자 순 -> 수정 일자가 Null이 아니면 걔 기준으로 하면 됨
    @Query("SELECT qb FROM QuestionBoard qb " +
            "ORDER by qb.modifiedAt DESC")
    Page<QuestionBoard> findAll(Pageable pageable);


    // user가 작성한 qboardID의 qboard 글이 있는지?
    @Query("SELECT qb FROM QuestionBoard qb " +
            "WHERE qb.user = :user and qb.qboardId = :qboardId")
    Optional<QuestionBoard> findByUserAndQboardId(@Param("user")User user, @Param("qboardId")Long qboardId);

    // 조회수 증가 쿼리
    @Modifying  // delete, update, insert 쿼리문에 대해서는 modifying을 꼭 붙여줘야 한다.
    @Query("UPDATE QuestionBoard q set q.viewCount = q.viewCount + 1 where q.qboardId = :qboardId")
    int updateViewCount(@Param("qboardId") Long qboardId);
}
