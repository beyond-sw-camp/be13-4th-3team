package com.beyond3.yyGang.answer.controller;


import com.beyond3.yyGang.answer.dto.AnswerRequestDto;
import com.beyond3.yyGang.answer.dto.AnswerResponseDto;
import com.beyond3.yyGang.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/qboard")
@RequiredArgsConstructor
@Tag(name = "Answer", description = "약 질문에 대한 답글")
public class AnswerController {
    private final AnswerService answerService;


    // 약 질문에 대한 답글 작성
    @PostMapping("/{qboardId}/answers")
    @Operation(summary = "답글 등록", description = "`약사`만 약 질문에 대한 답글 작성이 가능하다.")
    public ResponseEntity<AnswerResponseDto> saveAnswer(
            Principal principal,
            @PathVariable("qboardId") Long qboardId,
            @RequestBody AnswerRequestDto answerDto) {

        String userEmail = principal.getName();
        AnswerResponseDto answerResponseDto = answerService.saveAnswer(qboardId, answerDto, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(answerResponseDto);
    }


    // 이거 로그인한 회원만 볼 수 있게 할까?
    // 특정 질문에 대한 특정 답글을 조회하는건데 왜 리스트지
    @GetMapping("/{qboardId}/answers/{answerId}")
    @Operation(summary = "특정 답글 조회", description = "특정 질문에 대한 특정 답글 조회")
    public ResponseEntity<AnswerResponseDto> getAnswer(
            @PathVariable("qboardId") Long qboardId,
            @PathVariable("answerId") Long answerId) {

        AnswerResponseDto answer = answerService.getAnswerById(qboardId,answerId);

        return ResponseEntity.ok(answer);
    }


    // 페이징 처리 하면 좋겠는데 이것도
    @GetMapping("/{qboardId}/answers")
    @Operation(summary = "게시글별 전체 답글 조회", description = "특정 게시글에 달린 전체 답글을 조회 한다.")
    public ResponseEntity<List<AnswerResponseDto>> getAnswerByBoardId(
            @PathVariable("qboardId") Long qboardId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue="10") int size) {

        Page<AnswerResponseDto> answerList = answerService.getAnswerByBoardId(qboardId, page, size);

        return ResponseEntity.ok(answerList.getContent());
    }

    // 특정 질문에 대한 특정 답글 수정
    @PostMapping("/{qboardId}/answers/{answerId}")
    @Operation(summary = "답글 수정", description = "특정 질문에 대한 특정 답글을 수정")
    public ResponseEntity<AnswerResponseDto> updateAnswer(
            Principal principal,
            @PathVariable("qboardId") Long qboardId,
            @PathVariable("answerId") Long answerId,
            @RequestBody AnswerRequestDto requestDto) {

        String userEmail = principal.getName();
        AnswerResponseDto answerResponseDto = answerService.updateAnswer(qboardId, answerId, requestDto, userEmail);

        return ResponseEntity.ok(answerResponseDto);
    }

    @DeleteMapping("/{qboardId}/answers/{answerId}")
    @Operation(summary = "답글 삭제", description = "답글을 삭제 합니다.")
    public ResponseEntity<String> deleteAnswer(
            Principal principal,
            @PathVariable("qboardId") Long qboardId,
            @PathVariable("answerId") Long answerId) {

        String userEmail = principal.getName();
        answerService.deleteAnswer(qboardId,answerId, userEmail);

        return ResponseEntity.ok("답글이 삭제되었습니다.");
    }

}
