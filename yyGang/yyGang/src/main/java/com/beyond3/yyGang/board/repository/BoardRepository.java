package com.beyond3.yyGang.board.repository;

import com.beyond3.yyGang.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    Page<Board> findAll(Pageable pageable);

}
