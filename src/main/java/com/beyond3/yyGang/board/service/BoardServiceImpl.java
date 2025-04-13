package com.beyond3.yyGang.board.service;

import com.beyond3.yyGang.board.Board;
import com.beyond3.yyGang.board.dto.BoardPageResponseDto;
import com.beyond3.yyGang.board.dto.BoardUpdateRequestDto;
import com.beyond3.yyGang.board.repository.BoardLikeRepository;
import com.beyond3.yyGang.board.repository.BoardRepository;
import com.beyond3.yyGang.board.dto.BoardRequestDto;
import com.beyond3.yyGang.board.dto.BoardResponseDto;
import com.beyond3.yyGang.handler.exception.BoardException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;


    public User userByPrincipal(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 없음: "));
        return user;
    }

    public Board findBoardById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 없음"));
        return board;
    }

    public void validateOwner(User user, Board board) {
        if (!user.getUserId().equals(board.getUser().getUserId())) {
            throw new IllegalArgumentException("해당 게시글 작성자불일치");
        }
    }

    @Override
    @Transactional
    public void save(BoardRequestDto requestDto, Principal principal) {

        User user = userByPrincipal(principal);

        Board board = requestDto.toEntity(user);

        boardRepository.save(board);
    }

    @Override
    public BoardPageResponseDto findAll(int page, int size) {

        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("page, size가 유효하지 않음");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardPage = boardRepository.findAll(pageable);

        List<BoardResponseDto> boardResponseDto = boardPage.stream().map(BoardResponseDto::new).toList();


        BoardPageResponseDto boardPageResponseDto = new BoardPageResponseDto(boardResponseDto, boardPage.getTotalElements());
        if (boardPage.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않음");
        }
        return boardPageResponseDto;
    }

    @Override
    @Transactional
    public BoardResponseDto update(Principal principal, Long id, BoardUpdateRequestDto requestDto) {
        User user = userByPrincipal(principal);


        Board board = findBoardById(id);

        validateOwner(user, board);

        board.update(requestDto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    @Override
    public Board findById(Long id) {
        Board board = findBoardById(id);
        System.out.println(board);

        return board;
    }

    @Override
    @Transactional
    public void delete(Principal principal, Long id) {

        Board board = findBoardById(id);

        User user = userByPrincipal(principal);

        validateOwner(user, board);

        boardRepository.deleteById(id);
    }

    @Override
    public long getBoardLikeCount(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException());

        return boardLikeRepository.countByBoard(board);
    }
}
