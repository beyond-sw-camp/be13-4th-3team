package com.beyond3.yyGang.answer.repository;

import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.answer.domain.AnswerLike;
import com.beyond3.yyGang.answer.domain.AnswerLikedId;
import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, AnswerLikedId> {

    boolean existsById(AnswerLikedId id);

    // 좋아요 했는지 확인
    boolean existsByAnswer(Answer answer);

    // 특정 answer에 user가 좋아요 했는지 확인하기 위함
    Optional<AnswerLike> findByUserAndAnswer(User user, Answer answer);

    //좋아요 갯수 조회
    long countByAnswer_answerId(Long answerId);

    // 좋아요 삭제
//    void deleteByAnswer_answerId(AnswerLikedId id);

}
