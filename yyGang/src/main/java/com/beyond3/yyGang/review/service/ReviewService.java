package com.beyond3.yyGang.review.service;

import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.review.domain.Review;
import com.beyond3.yyGang.review.dto.ReviewPageResponseDto;
import com.beyond3.yyGang.review.dto.ReviewResponseDto;
import com.beyond3.yyGang.review.dto.ReviewRequestDto;
import com.beyond3.yyGang.review.repository.ReviewRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final NSupplementRepository nSupplementRepository;
    private final UserRepository userRepository;

    // 상품에 대한 리뷰 작성 로직
    @Transactional
    public ReviewResponseDto registerReview(String email, ReviewRequestDto reviewRequestDto, Long productId) {
        // 회원 추출
        User user = extractedUser(email);

        // 상품 추출 -> 해당 상품이 존재하는지 확인
        NSupplement product = findProduct(productId);

        // 해당 회원이 해당 상품에 대한 리뷰를 이미 작성했는지?
        Optional<Review> reviewByUser = reviewRepository.findByUserAndNSupplement(user, product);

        if(reviewByUser.isPresent()) {
            throw new NSupplementException(ExceptionMessage.ALREADY_REVIEWED);
        }

        // 리뷰를 작성하지 않았다면 -> 리뷰 저장
        Review review = reviewRequestDto.toEntity(user, product);
        // review 저장 + review 수 증가
        Review savedReview = reviewRepository.save(review);
        product.setReviewCount(product.getReviewCount() + 1);

        return new ReviewResponseDto(savedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(String email, Long productId) {
        // 사용자 추출
        User user = extractedUser(email);

        // 상품 추출
        NSupplement product = findProduct(productId);

        // 해당 회원이 해당 상품에 대해 작성한 리뷰 가져오기
        Review reviewByUser = reviewRepository.findByUserAndNSupplement(user, product)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.REVIEW_NOT_FOUND));

        // 리뷰를 찾았으면 리뷰 삭제하기 + 리뷰 수 감소
        reviewRepository.delete(reviewByUser);
        product.setReviewCount(product.getReviewCount() - 1);
    }

//    // 특정 상품에 대한 리뷰 조회
//    @Transactional
//    public List<ReviewResponseDto> viewReview(Long productId, int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        if(page < 0 || size <= 0){
//            throw new NSupplementException(ExceptionMessage.INVALID_VALUE);
//        }
//
//        // 상품 먼저 추출
//        NSupplement product = findProduct(productId);
//
//        // 특정 상품에 대한 리뷰들을 전부 가져오기
//        Page<Review> reviews = reviewRepository.findByNSupplement(product, pageable);
//
//        return reviews.stream().map(ReviewResponseDto::new).toList();
//    }

    @Transactional
    public ReviewPageResponseDto viewReview(Long productId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new NSupplementException(ExceptionMessage.INVALID_VALUE);
        }

        // 상품 먼저 추출
        NSupplement product = findProduct(productId);

        // 특정 상품에 대한 리뷰들을 전부 가져오기
        Page<Review> reviews = reviewRepository.findByNSupplement(product, pageable);

        List<ReviewResponseDto> reviewResponseDtos = reviews.stream().map(ReviewResponseDto::new).toList();

        ReviewPageResponseDto reviewPageResponseDto = new ReviewPageResponseDto(reviewResponseDtos, reviews.getTotalElements());

        return reviewPageResponseDto;
    }

    @Transactional
    public String getReview(String email, Long productId) {
        // 사용자 추출
        User user = extractedUser(email);

        // 상품 추출
        NSupplement product = findProduct(productId);

        // 작성한 리뷰 확인 -> 회원이 특정 상품에 대해 작성한 리뷰가 있는지?
        Review review = reviewRepository.findByUserAndNSupplement(user, product)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.REVIEW_NOT_FOUND));

        return review.getContent();
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto modifyReview(String email, Long productId, ReviewRequestDto reviewRequestDto) {
        // 사용자 추출
        User user = extractedUser(email);

        // 상품 추출
        NSupplement product = findProduct(productId);

        // 작성한 리뷰 확인 -> 회원이 특정 상품에 대해 작성한 리뷰가 있는지?
        Review review = reviewRepository.findByUserAndNSupplement(user, product)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.REVIEW_NOT_FOUND));

        review.setContent(reviewRequestDto.getContent());

        // 수정 후 수정 일자가 확인되도록 설정
        Review save = reviewRepository.save(review);

        return new ReviewResponseDto(save);
    }

    // 해당 email을 갖는 사용자가 존재하는지 확인
    private User extractedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

    // 해당 Id를 갖는 상품이 존재하는지 확인
    private NSupplement findProduct(Long productId) {
        return nSupplementRepository.findByproductId(productId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));
    }
}
