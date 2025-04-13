package com.beyond3.yyGang.comment;

import com.beyond3.yyGang.board.Board;
import com.beyond3.yyGang.board.repository.BoardRepository;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void save(Principal principal, Long boardId, CommentRequestDto requestDto, Long parentId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment parentComment = null;

        if (parentId != null) { // parentId가 있으면 부모 댓글 조회
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = requestDto.toEntity(user, board, parentComment);

        if (parentComment != null) {
            parentComment.addChild(comment); // 부모 댓글에 대댓글 추가
        }

        commentRepository.save(comment).getId();
    }

    @Override
    @Transactional
    public void update(Principal principal, Long id, CommentRequestDto requestDto) {

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 없으"));

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없음"));

        boolean x = user.getUserId() == comment.getUser().getUserId();
        if (!x){
            throw new IllegalArgumentException("작성자 불일치");
        }

        comment.update(requestDto);
    }

    @Override
    public Page<CommentResponseDto> getComments(int page, int size, Long boardId) {

        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("댓글이 없음");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> parentComments = commentRepository.findByBoardIdAndParentCommentIsNull(boardId, pageable);

        // 각 부모 댓글의 대댓글을 가져와서 DTO로 변환
        return parentComments.map(comment -> {
            List<Comment> childComments = commentRepository.findByParentCommentId(comment.getId());
            return CommentResponseDto.fromEntity(comment, childComments);
        });

    }

    @Override
    @Transactional
    public void delete(Principal principal, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없음"));

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 없으"));

        boolean x = user.getUserId() == comment.getUser().getUserId();
        if (!x){
            throw new IllegalArgumentException("작성자 불일치");
        }
        commentRepository.deleteById(id);
    }


}
