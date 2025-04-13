package com.beyond3.yyGang.nutrientAnswer.controller;

import com.beyond3.yyGang.nutrientAnswer.NAnswer;
import com.beyond3.yyGang.nutrientAnswer.dto.NurtientAnswerRequestDto;
import com.beyond3.yyGang.nutrientAnswer.dto.NutrientAnswerModifyDto;
import com.beyond3.yyGang.nutrientAnswer.dto.NutrientAnswerResponseDto;
import com.beyond3.yyGang.nutrientAnswer.service.NurtientAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/nSupplement")
@RequiredArgsConstructor
@Tag(name = "NAnswer", description = "상품 문의에 대한 답글")
public class NutrientAnswerController {

    private final NurtientAnswerService nurtientAnswerService;

    @PostMapping("/{nSupplementId}/NutrientQuestion/{nqboardId}")
    @Operation(summary = "문의 답변 등록", description = "특정 상품 문의에 대한 답변을 등록한다.")
    public ResponseEntity<NutrientAnswerResponseDto> saveNAnswer(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId,
            @RequestBody @Valid NurtientAnswerRequestDto requestDto) {

        String userEmail = principal.getName();
        NutrientAnswerResponseDto nutrientAnswerResponseDto = nurtientAnswerService.saveAnswer(nSupplementId, nqboardId, requestDto, userEmail);

        return ResponseEntity.ok(nutrientAnswerResponseDto);
    }

    // 상품 문의에 대한 답변 조회 -> 어차피 상품 등록자 한 명만 답변 쓸 수 있는거 아냐?
    @GetMapping("/{nSupplementId}/NutrientQuestion/{nqboardId}/answer")
    @Operation(summary = "답변 조회", description = "특정 상품의 문의에 달린 답글 전체 조회합니다.")
    public ResponseEntity<NutrientAnswerResponseDto> getAllAnswer(
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId) {

        NutrientAnswerResponseDto allnaswer = nurtientAnswerService.getAllNAnswer(nSupplementId, nqboardId);

        return ResponseEntity.ok(allnaswer);
    }

    // 특정 상품에 대한 문의에 대한 답변 -> 수정
    @PostMapping("/{nSupplementId}/NutrientQuestion/{nqboardId}/answer/{answerId}")
    @Operation(summary = "답변 수정", description = "특정 상품에 대한 문의사항에 대한 답변을 수정합니다.")
    public ResponseEntity<NutrientAnswerResponseDto> updateNAnswer(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId,
            @PathVariable("answerId") Long answerId,
            @RequestBody NutrientAnswerModifyDto requestDto) {

        String userEmail = principal.getName();
        NutrientAnswerResponseDto nutrientAnswerResponseDto = nurtientAnswerService.updateAnswer(userEmail, nSupplementId, nqboardId, answerId, requestDto);
        return ResponseEntity.ok(nutrientAnswerResponseDto);
    }

    // 특정 상품에 대한 문의에 대한 답변 -> 수정
    @DeleteMapping("/{nSupplementId}/NutrientQuestion/{nqboardId}/answer/{answerId}")
    @Operation(summary = "답변 삭제", description = "특정 상품에 대한 문의사항에 대한 답변을 삭제합니다..")
    public ResponseEntity<NutrientAnswerResponseDto> deleteAnswer(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId,
            @PathVariable("answerId") Long answerId,
            @RequestBody NutrientAnswerModifyDto requestDto) {

        String userEmail = principal.getName();
        nurtientAnswerService.deleteAnswer(userEmail, nSupplementId, nqboardId, answerId, requestDto);
        return ResponseEntity.ok().build();
    }

}
