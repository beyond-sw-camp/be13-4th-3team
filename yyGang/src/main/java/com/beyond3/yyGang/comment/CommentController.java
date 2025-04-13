package com.beyond3.yyGang.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponseDto> save (
            Principal principal,
            @PathVariable(value = "boardId") Long boardId,
            @RequestBody CommentRequestDto requestDto,
            @RequestParam(required = false) Long parentId // 대댓글일 경우 parentId 전달
    ){


        commentService.save(principal, boardId, requestDto, parentId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(Principal principal,@PathVariable Long id, @RequestBody CommentRequestDto requestDto){

        commentService.update(principal, id, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // 특정 게시글 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @PathVariable Long boardId){
        Page<CommentResponseDto> commentList = commentService.getComments(page, size, boardId);

        return ResponseEntity.ok(commentList.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(Principal principal, @PathVariable Long id){
        commentService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

}
