package com.beyond3.yyGang.nutrientQuestion.controller;

import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.nutrientQuestion.dto.NQuestionModifyDto;
import com.beyond3.yyGang.nutrientQuestion.service.NutrientQuestionService;
import com.beyond3.yyGang.nutrientQuestion.dto.NutrientQuestionRequestDto;
import com.beyond3.yyGang.nutrientQuestion.dto.NutrientQuestionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/NutrientQuestion")
@RequiredArgsConstructor
@Tag(name = "NQuestion", description = "상품 문의")
public class NutrientQuestionController {

    private final NutrientQuestionService nutrientQuestionService;

    @PostMapping("/{nSupplementId}")
    @Operation(summary = "상품 문의 등록하기", description = "특정 상품에 대한 문의를 등록합니다.")
    public ResponseEntity<NutrientQuestionResponseDto> saveNutrientQuestion(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @RequestBody @Valid NutrientQuestionRequestDto requestDto) {

        String userEmail = principal.getName();
        NutrientQuestionResponseDto resultDto = nutrientQuestionService.saveNquestion(requestDto, userEmail, nSupplementId);

        return ResponseEntity.ok(resultDto);
    }


    @GetMapping("/{nSupplementId}")
    @Operation(summary = "상품 문의 전체 조회", description = "특정 상품에 대한 문의 목록 전체를 조회 합니다.")
    public ResponseEntity<List<NutrientQuestionResponseDto>> getAllNutrientQuestion(
            @PathVariable("nSupplementId") Long nSupplementId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<NutrientQuestionResponseDto> nqboard = nutrientQuestionService.getAllNqboard(nSupplementId, page, size);

        return ResponseEntity.ok(nqboard.getContent());
    }

    @GetMapping("/{nSupplementId}/{nqboardId}")
    @Operation(summary = "특정 ID로 상품 문의 조회", description = "ID로 특정 상품에 대한 문의 사항을 조회한다.")
    public ResponseEntity<NutrientQuestionResponseDto> getNutrientQuestionById(
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId) {

        NutrientQuestionResponseDto nutrientQuestion = nutrientQuestionService.getNqboardById(nSupplementId, nqboardId);
        return ResponseEntity.ok(nutrientQuestion);
    }

    @PostMapping("/{nSupplementId}/{nqboardId}")
    @Operation(summary = "상품 문의 수정", description = "상품 문의를 수정 한다.")
    public ResponseEntity<Object> update(
            Principal principal,
            @RequestBody NQuestionModifyDto requestDto,
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId) {

        String userEmail = principal.getName();
        NutrientQuestionResponseDto resultDto = nutrientQuestionService.updateNqboard(nSupplementId, nqboardId, requestDto, userEmail);

        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{nSupplementId}/{nqboardId}")
    @Operation(summary = "상품 문의 삭제", description = "상품 문의를 삭제 합니다.")
    public ResponseEntity<String> delete(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @PathVariable("nqboardId") Long nqboardId) {

        String userEmail = principal.getName();
        nutrientQuestionService.deleteQboard(nSupplementId, nqboardId, userEmail);
        return  ResponseEntity.ok("문의사항이 삭제 되었습니다.");
    }
}
