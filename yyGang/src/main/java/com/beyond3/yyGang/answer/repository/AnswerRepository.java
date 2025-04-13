package com.beyond3.yyGang.answer.repository;


import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // answerId와 qboardId를 만족하는 answer가 있는지?
    @Query("select a FROM Answer a " +
            "where a.answerId = :answerId AND a.qboard.qboardId = :qboardId")
    Optional<Answer> findByAnswerIdAndQboard_QboardId(@Param("answerId")Long answerId, @Param("qboardId") Long questionId);

    // User, 게시글 Id를 바탕으로 답글 찾기
    @Query("select a FROM Answer a " +
            "where a.user = :user AND a.qboard.qboardId = :qboardId")
    Optional<Answer> findByUserAndQboard_QboardId(@Param("user") User user, @Param("qboardId") Long qboardQboardId);


    // 특정 Id로 answer 조회하는데 정렬 기준은 생성일자 및 수정일자
    @Query("SELECT a FROM Answer a " +
            "where a.qboard.qboardId = :qboardId " +
            "order by COALESCE(a.modifiedAt, a.createdAt) DESC")
    Page<Answer> findByQboard_QboardId(@Param("qboardId") Long qboardQboardId, Pageable pageable);
}
