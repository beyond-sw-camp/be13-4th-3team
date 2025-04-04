package com.beyond3.yyGang.cart.controller;

import com.beyond3.yyGang.cart.dto.AddCartOptionRequestDto;
import com.beyond3.yyGang.cart.dto.CartOptionDto;
import com.beyond3.yyGang.cart.dto.CartResponseDto;
import com.beyond3.yyGang.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "장바구니 관련 기능")
public class CartController {

    private final CartService cartService;


    // 로그인한 사용자의 장바구니 조회 -> 페이징으로 처리하는게 낫겠지?
    @GetMapping
    @Operation(summary = "장바구니 목록 조회", description = "사용자 장바구니의 영양제 목록을 조회한다.")
    public ResponseEntity<CartResponseDto> getUserCart(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        String userEmail = principal.getName();
        CartResponseDto cartResponseDto = cartService.getCart(userEmail, page, size);

        return ResponseEntity.ok(cartResponseDto);
    }


    @PostMapping("/nsupplement")
    @Operation(summary = "장바구니 영양제 추가", description = "사용자 장바구니에 영양제를 추가한다.")
    public ResponseEntity<CartResponseDto> addCartOption(
            Principal principal,
            @Valid @RequestBody AddCartOptionRequestDto addCartOptionRequestDto
    ) {
        String userEmail = principal.getName();

        CartResponseDto cartResponseDto = cartService.addCartOption(userEmail, addCartOptionRequestDto);

        return ResponseEntity.ok(cartResponseDto);
    }


    // 장바구니에서 특정 영양제 삭제
    @DeleteMapping("/{cartOptionId}")
    @Operation(summary = "장바구니 영양제 삭제", description = "사용자 장바구니에 있는 특정 영양제를 삭제한다.")
    public ResponseEntity<String> deleteCartOption(
            Principal principal,
            @PathVariable("cartOptionId") Long cartOptionId
    ) {
        String email = principal.getName();
        cartService.deleteCartOption(cartOptionId, email);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }


    // 장바구니 상품 수량,가격 변경
    @PutMapping("/{cartOptionId}")
    @Operation(summary = "장바구니 영양제 수량 변경", description = "사용자 장바구니의 특정 영양제 수량을 변경한다.")
    public ResponseEntity<CartOptionDto> updateCartOptionQuantity(
            @PathVariable("cartOptionId") Long cartOptionId,
            @RequestParam("quantity") int quantity, // 수량 변경, 최소 1개
            Principal principal) {

        String userEmail = principal.getName();
        CartOptionDto cartOptionDto = cartService.updateCartProduct(cartOptionId, quantity, userEmail);
        return ResponseEntity.ok(cartOptionDto);
    }

}
