package com.beyond3.yyGang.handler.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    ORDER_NOT_EXIST("주문이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    OUT_OF_STOCK("재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
    NO_ITEMS_IN_CART("장바구니가 비었습니다.", HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND_IN_ORDER("주문 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CANNOT_CANCEL_ORDER("주문 취소가 불가능합니다.", HttpStatus.BAD_REQUEST),
    CANNOT_FOUND_PAY_INFO("결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS("이미 상품이 존재합니다.", HttpStatus.CONFLICT),

    INVALID_CREDENTIALS("이메일 혹은 비밀번호가 틀립니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND("사용자의 장바구니를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_EXIST("이미 계좌가 존재합니다.", HttpStatus.CONFLICT),
    ACCOUNT_NOT_EXIST("계좌가 존재하지 않습니다.", HttpStatus.NOT_FOUND ),
    CANNOT_SELECT_ADMIN("관리자 권한을 부여할 수 없습니다.", HttpStatus.UNAUTHORIZED),

    INVALID_REFRESH_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("만료된 토큰입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_TOKEN("지원되지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_TOKEN("JWT 토큰이 비어 있습니다.", HttpStatus.BAD_REQUEST),

    ONLY_SELLER_CAN_ACCESS("해당 작업에 대해 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    INGREDIENT_NOT_FOUND("영양 성분에 대한 정보가 없습니다.", HttpStatus.NOT_FOUND ),
    NO_REGISTERED_PRODUCTS("등록된 상품이 없습니다.", HttpStatus.NOT_FOUND),
    HEALTH_FUNCTIONAL_ITEM_NOT_FOUND("건강 기능 항목에 대한 정보가 없습니다.", HttpStatus.NOT_FOUND),
    CANNOT_FOUND_PRODUCTS("상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_REVIEWED("이미 이 상품에 대한 리뷰를 작성했습니다.", HttpStatus.CONFLICT),
    REVIEW_NOT_FOUND("리뷰를 작성하지 않았습니다.", HttpStatus.NOT_FOUND),

    INVALID_VALUE("유효한 값이 아닙니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_QUESTION_BOARD("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ONLY_PHARMACIST_CAN_ANSWER("약사만 답글을 달 수 있습니다.", HttpStatus.BAD_REQUEST),
    QUESTION_ALREADY_ANSWERED("이미 답글을 달았습니다.", HttpStatus.CONFLICT),
    NOT_FOUND_ANSWER("답글을 찾을 수 없습니다.", HttpStatus.CONFLICT),
    CANNOT_EDIT_OTHER_ANSWERS("본인이 작성한 답글에 대해서만 수정, 삭제가 가능합니다.", HttpStatus.BAD_REQUEST),
    ANSWER_ALREADY_LIKED("이미 좋아요를 누른 답변입니다.", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_LIKED("좋아요를 누른 적 없는 답변입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INQUIRY_NOT_FOUND("해당 상품에 대한 문의사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    CANNOT_FOUND_ANSWER("해당 문의사항에 대한 답변을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CANNOT_EDIT_CONTENTS("게시글 수정 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    NO_POSTS_EXIST("게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CANNOT_FOUND_BOARD("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
