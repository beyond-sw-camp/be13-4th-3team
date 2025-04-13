package com.beyond3.yyGang.user.repository;

import com.beyond3.yyGang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 회원 아이디 - 단건 조회
    User findByUserId(Long id);

    // 회원 이메일 조회
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("SELECT u FROM User u ORDER BY u.email DESC")
    List<User> findAllDesc();

    User findEmailByUserId(Long userId);
}
