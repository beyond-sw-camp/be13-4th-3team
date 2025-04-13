package com.beyond3.yyGang.board.controller;

import com.beyond3.yyGang.board.service.BoardLikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @PostMapping("/{boardId}")
    @Operation(summary = "좋아요", description = "좋아요 등록 취소")
    public ResponseEntity<String> insert(
            Principal principal,
            @PathVariable("boardId")Long boardId ){
        return ResponseEntity.ok(boardLikeService.insert(principal,boardId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> getLikeInfo(Principal principal, @PathVariable("id") Long boardId){

        return ResponseEntity.ok(boardLikeService.getLikeInfo(principal, boardId));
    }

}
