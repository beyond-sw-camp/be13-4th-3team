package com.beyond3.yyGang.nutrientQuestion.repository;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NutrientQuestionRepository extends JpaRepository<NQuestion, Long> {

    Page<NQuestion> findBySupplement(@Param("supplement") NSupplement supplement, Pageable pageable);

    Optional<NQuestion> findByQuestionIdAndUser(Long questionId, User user);

    Optional<NQuestion> findBySupplementAndUser(NSupplement supplement, User user);

    Optional<NQuestion> findByQuestionIdAndSupplement(Long questionId, NSupplement supplement);


    @Query("select n " +
            "from NQuestion n " +
            "join fetch n.supplement ns " +
            "where ns.seller.email = :userEmail " +
                    "and ns.productId = :nSupplementId " +
                    "and n.questionId = :nQboardId")
    Optional<NQuestion> verifySellerAndNQboard(
            @Param("userEmail") String userEmail,
            @Param("nSupplementId") Long nSupplementId,
            @Param("nQboardId") Long nQboardId
    );
}
