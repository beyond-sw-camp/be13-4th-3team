package com.beyond3.yyGang.board.service;

import com.beyond3.yyGang.board.Board;
import com.beyond3.yyGang.board.BoardLike;
import com.beyond3.yyGang.board.BoardLikeId;
import com.beyond3.yyGang.board.repository.BoardLikeRepository;
import com.beyond3.yyGang.board.repository.BoardRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;


    @Override
    public String insert(Principal principal, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 없음"));

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("일치하지 않음"));

        BoardLikeId boardLikeId = new BoardLikeId(user.getUserId(), board.getId());
        BoardLike boardLike = new BoardLike(boardLikeId, user, board);

        if (boardLikeRepository.existsByUserAndBoard(user, board)) {
            boardLikeRepository.delete(boardLike);
            return "좋아요 취소";
        }

        boardLikeRepository.save(boardLike);

        return "좋아요 등록";
    }

    @Override
    public Boolean getLikeInfo(Principal principal, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 없음"));

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("일치하지 않음"));

        Boolean likeInfo = boardLikeRepository.existsByUserAndBoard(user, board);
        return likeInfo;
    }

}
