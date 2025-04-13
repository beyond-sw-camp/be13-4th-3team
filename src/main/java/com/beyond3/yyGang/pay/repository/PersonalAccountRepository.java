package com.beyond3.yyGang.pay.repository;

import com.beyond3.yyGang.pay.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {

    //페치 조인 사용 고려
    //@Query("select pa from PersonalAccount pa join fetch User u where pa.user.userId = :userId")
    @Query("select pa from PersonalAccount pa where pa.user.userId = :userId")
    Optional<PersonalAccount> findByUserId(@Param("userId") Long userId);
}