package com.beyond3.yyGang.comment;

import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface CommentService {

    void save(Principal principal, Long boardId, CommentRequestDto requestDto, Long parentId);

    void update(Principal principal, Long id, CommentRequestDto requestDto);

    Page<CommentResponseDto> getComments(int page, int size, Long boardId);

    void delete(Principal principal,Long id);
}
