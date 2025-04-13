package com.beyond3.yyGang.q_board.controller;

import com.beyond3.yyGang.q_board.dto.QboardPageResponseDto;
import com.beyond3.yyGang.q_board.service.QuestionBoardService;
import com.beyond3.yyGang.q_board.dto.QboardRequestDto;
import com.beyond3.yyGang.q_board.dto.QboardResponseDto;
import com.beyond3.yyGang.q_board.dto.QboardUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/qboard")
@RequiredArgsConstructor
@Tag(name="Qboard", description = "약 질문 게시판")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    //  게시글 작성
    //      - 게시글 작성 후 엔티티를 반환하는 것이 아닌, Dto로 변환해서 해당 작성 내용을 보여주는 편이 나음
    @PostMapping
    @Operation(summary = "게시글 등록", description = "게시글을 등록한다.")
    public ResponseEntity<QboardResponseDto> saveQuestionBoard(
            Principal principal,
            @RequestBody QboardRequestDto requestDto) {

        String userEmail = principal.getName();
        QboardResponseDto qboardResponseDto = questionBoardService.saveQboard(requestDto, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qboardResponseDto);
    }

    // 게시글 조회   -> 날짜 순으로 조회
    @GetMapping
    @Operation(summary = "전체 게시글 조회", description = "전체 게시글을 조회한다.")
    public ResponseEntity<QboardPageResponseDto> getAllQuestionBoard(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        QboardPageResponseDto allQboard = questionBoardService.getAllQboard(page, size);
        return ResponseEntity.ok(allQboard);
    }


    // 특정 ID로 게시글 조회  ->  게시글 클릭 시 넘어가는 로직으로 생각해도 될듯
    @GetMapping("/{qboardId}")
    @Operation(summary = "특정 ID 게시글 조회", description = "특정 ID로 게시글을 조회한다.")
    public ResponseEntity<QboardResponseDto> getQuestionBoardById(
            @PathVariable("qboardId") Long qboardId) {

        QboardResponseDto questionBoard = questionBoardService.getQboardById(qboardId);

        return ResponseEntity.ok(questionBoard);
    }


    // 특정 페이지 게시글 수정
    @PostMapping("/{qboardId}")
    @Operation(summary = "게시글 수정", description = "해당 게시글의 작성자가 게시글을 수정한다.")
    public ResponseEntity<QboardResponseDto> update(
            Principal principal,
            @RequestBody QboardUpdateDto requestDto,
            @PathVariable("qboardId") Long qboardId) {

        String userEmail = principal.getName();
        QboardResponseDto qboardResponseDto = questionBoardService.updateQboard(qboardId, requestDto, userEmail);

        return ResponseEntity.ok(qboardResponseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{qboardId}")
    @Operation(summary = "게시글 삭제", description = "해당 게시글의 작성자가 게시글을 삭제한다.")
    public ResponseEntity<String> delete(
            Principal principal,
            @PathVariable("qboardId") Long qboardId) {

        String userEmail = principal.getName();
        questionBoardService.deleteQboard(qboardId, userEmail);

        return  ResponseEntity.ok("성공적으로 삭제 되었습니다.");
    }

}
