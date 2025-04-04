package com.beyond3.yyGang.review.repository;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.review.domain.Review;
import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "WHERE r.nSupplement = :nSupplement " +
            "ORDER BY r.modifiedAt DESC ")
    Page<Review> findByNSupplement(@Param("nSupplement") NSupplement nSupplement, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.nSupplement = :nSupplement AND r.user = :user")
    Optional<Review> findByUserAndNSupplement(@Param("user") User user, @Param("nSupplement") NSupplement nSupplement);

}
