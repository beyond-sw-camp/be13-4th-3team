package com.beyond3.yyGang.nutrientAnswer.repository;

import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.nutrientAnswer.NAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurtientAnswerRepository extends JpaRepository<NAnswer, Long> {


    @Query("select n from NAnswer n where n.user.email = :userEmail and n.question.questionId = :questionId")
    Optional<NAnswer> verifyNAnswer(@Param("userEmail") String userEmail, @Param("questionId") Long questionId);


    @Query("select n " +
            "from NAnswer n " +
            "join fetch n.question nq " +
            "join fetch nq.supplement ns " +
            "where ns.productId = :nSupplementId and nq.questionId = :nqboardId and n.answerId = :answerId")
    Optional<NAnswer> getAnswer(@Param("nSupplementId") Long nSupplementId,
                              @Param("nqboardId") Long nqboardId,
                              @Param("answerId") Long answerId);

    @Query("select n " +
            "from NAnswer n " +
            "join fetch n.question nq " +
            "join fetch nq.supplement ns " +
            "where ns.productId =:nSupplementId and nq.questionId = :nqboardId " +
            "")
    Optional<NAnswer> getAllAnswer(@Param("nSupplementId")Long nSupplementId,
                              @Param("nqboardId")Long nqboardId);
}
