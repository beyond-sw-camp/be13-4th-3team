package com.beyond3.yyGang.board.service;

import java.security.Principal;

public interface BoardLikeService {
    String insert(Principal principal, Long boardId);

    Boolean getLikeInfo(Principal principal, Long boardId);

}
