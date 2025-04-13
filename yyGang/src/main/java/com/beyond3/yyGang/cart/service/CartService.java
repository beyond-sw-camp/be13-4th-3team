package com.beyond3.yyGang.cart.service;

import com.beyond3.yyGang.cart.domain.Cart;
import com.beyond3.yyGang.cart.domain.CartOption;
import com.beyond3.yyGang.cart.dto.AddCartOptionRequestDto;
import com.beyond3.yyGang.cart.dto.CartOptionDto;
import com.beyond3.yyGang.cart.dto.CartResponseDto;
import com.beyond3.yyGang.cart.repository.CartOptionRepository;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.handler.exception.CartEntityException;
import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.exception.OrderException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartOptionRepository cartOptionRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final NSupplementRepository nSupplementRepository;

    @Transactional
    // 장바구니에 상품 추가
    public CartResponseDto addCartOption(String userEmail, AddCartOptionRequestDto addCartOptionRequestDto) {

        // 사용자 확인
        User findUser = getUser(userEmail);

        // 사용자 이메일을 통해 카트 확인
        Cart findCart = cartRepository.findByUserEmail(findUser.getEmail())
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 상품 Id를 통해 해당 상품이 있는지 확인
        NSupplement findNSupplement = nSupplementRepository.findById(addCartOptionRequestDto.getNSupplementId())
                .orElseThrow(() -> new OrderException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        // 해당 아이디의 상품이 이미 cart에 존재하는지 확인
        Optional<CartOption> byCartIdAndNSupplement = cartOptionRepository.findByCartIdAndNSupplement(findCart.getCartId(), findNSupplement);
        if (byCartIdAndNSupplement.isPresent()) {
            // Cart에 해당 상품이 이미 있으면 예외 던지기
            throw new CartEntityException(ExceptionMessage.PRODUCT_ALREADY_EXISTS);
        }

        // 재고 확인
        if (findNSupplement.getStockQuantity() < addCartOptionRequestDto.getQuantity()) {
            throw new CartEntityException(ExceptionMessage.OUT_OF_STOCK);
        }

        // 카트 옵션 생성
        CartOption cartOption = CartOption.createCartOption(findCart, findNSupplement, addCartOptionRequestDto.getQuantity());

        // 카트 옵션 저장
        CartOption saveCartOption = cartOptionRepository.save(cartOption);

        // 카트 옵션 DTO 생성
        CartOptionDto cartOptionDto = CartOptionDto.fromCartOption(saveCartOption);
        int totalCount = findCart.getCartOptions().size();
        // 카트 DTO 리턴
        return CartResponseDto.fromCart(findCart.getCartId(), List.of(cartOptionDto), totalCount);
    }

    @Transactional
    public void deleteCartOption(Long cartOptionId, String userEmail) {
        // 사용자 확인
        User findUser = getUser(userEmail);

        // CartOption에서 id로 찾기
        CartOption findCartOption = cartRepository.verifyCartOption(findUser.getEmail(), cartOptionId)
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.NO_ITEMS_IN_CART));

        cartOptionRepository.delete(findCartOption);
    }

    @Transactional
    // 장바구니 상품 수량, 가격 업데이트
    public CartOptionDto updateCartProduct(Long cartOptionId, int count, String userEmail) {

        // 사용자 확인
        User findUser = getUser(userEmail);

        // CartOption에서 해당 CartOptionId를 갖는, findUser가 등록한 cart 상품 조회
        CartOption cartOption = cartRepository.verifyCartOption(findUser.getEmail(), cartOptionId)
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.NO_ITEMS_IN_CART));

        // cartOption에서 nSupplement 추출 -> 해당 상품이 아직 제대로 존재하는 상품인지? => 수량 업데이트 하기 전에 이미 삭제되어 있으면?
        NSupplement nSupplement = nSupplementRepository.findById(cartOption.getNSupplement().getProductId())
                .orElseThrow(() -> new OrderException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        // 재고 확인
        if (nSupplement.getStockQuantity() < count) {
            throw new OrderException(ExceptionMessage.OUT_OF_STOCK);
        }

        cartOption.updateSupplement(count);
        return CartOptionDto.fromCartOption(cartOption);
    }

    @Transactional
    public CartResponseDto getCart(String userEmail, int page, int size) {

        // 페이징 처리를 위해 입력받은 page, size 값이 유효한지 확인
        if(page < 0 || size <= 0){
            throw new CartEntityException(ExceptionMessage.INVALID_VALUE);
        }

        Pageable pageable = PageRequest.of(page-1, size);

        // 사용자 검증 우선
        User findUser = getUser(userEmail);

        // 해당 사용자의 Cart가 있는지 확인
        Cart byUserEmail = cartRepository.findByUserEmail(findUser.getEmail())
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 카트에 있는 카트옵션들을 가져올거라 패치조인 적용
//        Cart findCart = cartRepository.findByUserEmailWithCartOptions(userEmail)
//                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 페이징 처리
        Page<CartOption> findCart = cartRepository.findCartOptionByUserEmail(userEmail, pageable);

        // 찾아온 Cart에서 CartOptions 추출 -> Dto로 변경해서 반환
        List<CartOptionDto> cartOptionDtoList = findCart.getContent().stream()
                                                        .map(CartOptionDto::fromCartOption)
                                                        .toList();

        return CartResponseDto.fromCart(byUserEmail.getCartId(), cartOptionDtoList, (int) findCart.getTotalElements());
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}