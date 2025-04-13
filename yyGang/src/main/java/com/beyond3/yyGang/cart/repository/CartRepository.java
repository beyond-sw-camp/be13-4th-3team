package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.domain.Cart;
import com.beyond3.yyGang.cart.domain.CartOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 페치 조인 사용 고려, 아마 사용해야할듯
    @Query("select c from Cart c where c.user.userId = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

    @Query("select c from Cart c where c.user.email = :userEmail")
    Optional<Cart> findByUserEmail(@Param("userEmail") String email);

    // join fetch -> 패치 조인 적용 => 여러 번의 쿼리 대신 한 번의 쿼리로 필요한 데이터를 가져올 수 있음
    // UserEmail을 통해 Cart 엔티티를 조회하면서 CartOption도 함께 조회함
    @Query("select c from Cart c join fetch c.cartOptions co join fetch co.nSupplement ns where c.user.email = :userEmail")
    Optional<Cart> findByUserEmailWithCartOptions(@Param("userEmail") String email);

    @Query("select co " +
            "from CartOption co " +
            "join fetch co.cart c " +
            "where c.user.email = :userEmail")
    Page<CartOption> findCartOptionByUserEmail(@Param("userEmail") String email, Pageable pageable);

    @Query("select co from CartOption co join fetch co.cart c where c.user.email = :userEmail and co.cartOptionID = :cartOptionId")
    Optional<CartOption> verifyCartOption(@Param("userEmail") String email, @Param("cartOptionId") Long cartOptionId);
}