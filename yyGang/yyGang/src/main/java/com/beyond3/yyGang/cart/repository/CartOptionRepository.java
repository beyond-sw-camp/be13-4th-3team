package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.domain.CartOption;
import com.beyond3.yyGang.nsupplement.NSupplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartOptionRepository extends JpaRepository<CartOption, Long> {

    @Query("select co from CartOption co where co.cart.cartId = :cartId")
    List<CartOption> findByCartId(@Param("cartId") Long cartId);


    @Query("select co from CartOption co where co.cart.cartId = :cartId and co.nSupplement = :nsupplement")
    Optional<CartOption> findByCartIdAndNSupplement(@Param("cartId")Long cartId, @Param("nsupplement")NSupplement nSupplement);

}