package com.beyond3.yyGang.nsupplement.controller;
import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementDetailResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementModifyDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDtoV2;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDtoV2;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nsupplement.repository.SortType;
import com.beyond3.yyGang.nsupplement.service.NSupplementService;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.review.dto.ReviewPageResponseDto;
import com.beyond3.yyGang.review.dto.ReviewRequestDto;
import com.beyond3.yyGang.review.dto.ReviewResponseDto;
import com.beyond3.yyGang.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nsupplement")
@RequiredArgsConstructor
@Tag(name = "Nsupplement", description = "영양제 관련 기능")
public class NSupplementController {

    // 확인용
    private final NSupplementService nSupplementService;
    private final NSupplementRepository nSupplementRepository;
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "상품 등록", description = "SELLER 만 상품 등록이 가능하다.")
    public ResponseEntity<String> register(
            Principal principal,
            @RequestBody @Valid NSupplementRegisterDto nSupplementRegisterDto) {

        String email = principal.getName();
        nSupplementService.registerProduct(email, nSupplementRegisterDto);

        return ResponseEntity.ok("상품 등록이 완료되었습니다.");
    }

    @GetMapping
    @Operation(summary = "상품 확인", description = "판매자만 상품 조회 가능 / 본인이 등록한 상품만 조회 가능")
    // Seller 입장에서 등록한 상품들 확인
    public ResponseEntity<List<NSupplementResponseDto>> info(
            Principal principal
    ) {
        String email = principal.getName();
        List<NSupplementResponseDto> allNSupplements = nSupplementService.getNSupplementsBySeller(email);

        return ResponseEntity.ok(allNSupplements);
    }

    @GetMapping("/info")
    public ResponseEntity<List<NSupplementRegisterDto>> info() {

        return ResponseEntity.ok(nSupplementService.getAllNSupplements());
    }

    @GetMapping("/productImage/{nSupplementId}")
    public ResponseEntity<String> getProductImage(@PathVariable Long nSupplementId) {

        String productImageByProductId = nSupplementRepository.findProductImageByProductId(nSupplementId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.CANNOT_FOUND_PRODUCTS));

        return ResponseEntity.ok(productImageByProductId);
    }

    @GetMapping("/{nSupplementId}")
    public ResponseEntity<NSupplementDetailResponseDto> detail(@PathVariable Long nSupplementId) {
        NSupplement nSupplement = nSupplementRepository.findByproductId(nSupplementId).orElseThrow(() -> new RuntimeException("nSupplement not found"));

        NSupplementDetailResponseDto nSupplementDetailResponseDto = new NSupplementDetailResponseDto(nSupplement.getProductId(), nSupplement.getProductName(), nSupplement.getCaution(), nSupplement.getBrand(), nSupplement.getPrice(), nSupplement.getReviewCount(), nSupplement.getProductImage());

        return ResponseEntity.ok(nSupplementDetailResponseDto);
    }


    @DeleteMapping("/{nSupplementId}")
    @Operation(summary = "상품 삭제", description = "해당 상품을 등록한 판매자만 삭제 가능")
    public ResponseEntity<String> delete(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId
    ){
        String email = principal.getName();
        nSupplementService.deleteProduct(email, nSupplementId);

        return ResponseEntity.ok("상품 삭제가 완료되었습니다.");
    }

    @PostMapping("/{nSupplementId}")
    @Operation(summary = "상품 수정", description = "해당 상품을 등록한 판매자만 수정 가능")
    public ResponseEntity<String> modify(
            Principal principal,
            @PathVariable("nSupplementId") Long nSupplementId,
            @RequestBody NSupplementModifyDto nSupplementModifyDto
    ){
        String email = principal.getName();
        log.info("modify: {}", nSupplementModifyDto);
        nSupplementService.modifyProduct(email, nSupplementId,nSupplementModifyDto);

        return ResponseEntity.ok("상품 수정이 완료되었습니다.");
    }

//    // 특정 상품 리뷰 조회  ->  페이징 처리
//    @GetMapping("/{nSupplementId}/review")
//    @Operation(summary = "상품 리뷰 조회", description = "특정 상품에 대한 모든 리뷰 조회")
//    public ResponseEntity<List<ReviewResponseDto>> viewReview(
//            @PathVariable("nSupplementId") Long productId,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "10") int size) {
//
//        // 특정 상품에 대한 리뷰이기 때문에 사용자는 필요 x
//        List<ReviewResponseDto> reviewResponseDtos = reviewService.viewReview(productId, page, size);
//
//        // 전체 리뷰들이 보이도록
//        return ResponseEntity.ok(reviewResponseDtos);
//    }

    @GetMapping("/{nSupplementId}/review")
    @Operation(summary = "상품 리뷰 조회", description = "특정 상품에 대한 모든 리뷰 조회")
    public ResponseEntity<ReviewPageResponseDto> viewReview(
            @PathVariable("nSupplementId") Long productId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // 특정 상품에 대한 리뷰이기 때문에 사용자는 필요 x
        ReviewPageResponseDto reviewPageResponseDto = reviewService.viewReview(productId, page, size);

        // 전체 리뷰들이 보이도록
        return ResponseEntity.ok(reviewPageResponseDto);
    }
    @GetMapping("/{nSupplementId}/review/my")
    @Operation(summary = "내 리뷰 조회", description = "특정 회원이 작성한 상품 리뷰 조회")
    public ResponseEntity<String> getReview(
            Principal principal,
            @PathVariable("nSupplementId") Long productId) {

        String reviewContent = reviewService.getReview(principal.getName(), productId);

        return ResponseEntity.ok(reviewContent);
    }

    // 리뷰 삭제
    @DeleteMapping("/{nSupplementId}/review")
    @Operation(summary = "리뷰 삭제", description = "특정 회원이 작성한 특정 상품에 대한 리뷰 삭제")
    public ResponseEntity<String> deleteReview(
            Principal principal,
            @PathVariable("nSupplementId") Long productId) {

        String email = principal.getName();
        reviewService.deleteReview(email, productId);

        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

    // 리뷰 작성
    @PostMapping("/{nSupplementId}/review")
    @Operation(summary = "리뷰 작성", description = "특정 회원이 특정 상품에 대한 리뷰 작성")
    public ResponseEntity<ReviewResponseDto> registerReview(
            Principal principal,
            @PathVariable("nSupplementId") Long productId,
            @RequestBody @Valid ReviewRequestDto reviewRequestDto) {

        String email = principal.getName();
        ReviewResponseDto reviewResponseDto = reviewService.registerReview(email, reviewRequestDto, productId);

        // 리뷰 작성 후 본인이 작성한 리뷰가 보이도록
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewResponseDto);
    }

    // 특정 상품 리뷰 수정
    @PutMapping("/{nSupplementId}/review")
    @Operation(summary = "리뷰 수정", description = "특정 사용자가 작성한 특정 상품에 대한 리뷰 수정")
    public ResponseEntity<ReviewResponseDto> modifyReview(
            Principal principal,
            @PathVariable("nSupplementId") Long productId,
            @RequestBody @Valid ReviewRequestDto reviewRequestDto) {

        String email = principal.getName();
        ReviewResponseDto reviewResponseDto = reviewService.modifyReview(email, productId, reviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewResponseDto);
    }

    @GetMapping("/info/search")
    public ResponseEntity<PageResponseDto<NSupplementResponseDtoV2>> infoSearch(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) List<Long> healthIds,
            @RequestParam(required = false) List<Long> ingredientIds,
            // DB 컬럼명이 아니라 엔티티 필드명을 기준으로 정렬, 일단 기본 정렬은 SortType.requestSortType 메소드에 설정함
            // size = -1 or page = -1 처럼 음수가 들어오는 상황 예외처리 할지, 너무 큰 값이 들어오면 max값 제한할지
            @PageableDefault(size = 10, page = 0/*, sort = "price", direction = Sort.Direction.ASC*/) Pageable pageable) {

        NSupplementSearchRequestDtoV2 nSupplementSearchRequestDtoV2 = new NSupplementSearchRequestDtoV2(productName, healthIds, ingredientIds, sortType);
        PageResponseDto<NSupplementResponseDtoV2> page = nSupplementRepository.searchPageV2(nSupplementSearchRequestDtoV2, pageable, SortType.requestSortType(nSupplementSearchRequestDtoV2.getSortType()));
        log.info(page.toString());
        return ResponseEntity.ok(page);
    }
}
