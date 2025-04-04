package com.beyond3.yyGang.nutrientQuestion.service;

import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.exception.QuestionBoardException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.nutrientQuestion.dto.NQuestionModifyDto;
import com.beyond3.yyGang.nutrientQuestion.repository.NutrientQuestionRepository;
import com.beyond3.yyGang.nutrientQuestion.dto.NutrientQuestionRequestDto;
import com.beyond3.yyGang.nutrientQuestion.dto.NutrientQuestionResponseDto;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NutrientQuestionService {

    private final NutrientQuestionRepository nutrientQuestionRepository;
    private final UserRepository userRepository;
    private final NSupplementRepository nSupplementRepository;


    // 특정 상품에 대한 문의 저장
    @Transactional
    public NutrientQuestionResponseDto saveNquestion(NutrientQuestionRequestDto requestDto, String userEmail, Long nSupplementId) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 특정 아이디의 상품 존재 여부 확인
        NSupplement supplement = nSupplementRepository.findById(nSupplementId)
                .orElseThrow(()->new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        NQuestion question = requestDto.toEntity(supplement,user);
        NQuestion save = nutrientQuestionRepository.save(question);

        return new NutrientQuestionResponseDto(save);
    }


    // 특정 상품에 대한 문의 내용들 확인 -> 페이징 처리까지
    @Transactional
    public Page<NutrientQuestionResponseDto> getAllNqboard(Long nSupplementId, int page, int size) {

        // 페이징 처리를 위해 받은 값이 유효한지 아닌지 확인
        if(page < 0 || size <= 0 ){
            throw new NSupplementException(ExceptionMessage.INVALID_VALUE);
        }

        Pageable pageable = PageRequest.of(page, size);

        // 특정 아이디의 상품 존재 여부 확인
        NSupplement supplement = nSupplementRepository.findById(nSupplementId)
                .orElseThrow(()->new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        // 해당 상품에 대한 문의 사항들
        Page<NQuestion> Nqboardpage = nutrientQuestionRepository.findBySupplement(supplement, pageable);

        return Nqboardpage.map(NutrientQuestionResponseDto::new);
    }

    // 문의 사항 단 건 조회 -> 특정 상품에 대한 특정 문의사항
    @Transactional
    public NutrientQuestionResponseDto getNqboardById(Long nSupplementId, Long nqboardId) {

        // 특정 아이디의 상품 존재 여부 확인
        NSupplement supplement = nSupplementRepository.findById(nSupplementId)
                .orElseThrow(()->new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        NQuestion Nqboard = nutrientQuestionRepository.findByQuestionIdAndSupplement(nqboardId, supplement)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        return new NutrientQuestionResponseDto(Nqboard);
    }

    // 문의 사항 수정
    @Transactional
    public NutrientQuestionResponseDto updateNqboard(Long nSupplementId, Long nqboardId, NQuestionModifyDto requestDto, String userEmail) {

        NQuestion nQuestion = verifyNQuestion(nSupplementId, nqboardId, userEmail);
        nQuestion.update(requestDto.getContent());

        return new NutrientQuestionResponseDto(nQuestion);
    }

    // 문의 사항 삭제
    public void deleteQboard(Long nSupplementId, Long nqboardId, String userEmail) {

        NQuestion nQuestion = verifyNQuestion(nSupplementId, nqboardId, userEmail);

        nutrientQuestionRepository.delete(nQuestion);
    }

    // 해당 문의 사항이 특정 사용자가 특정 상품에 대해 작성한 문의사항이 맞는지?
    public NQuestion verifyNQuestion(Long nSupplementId, Long nQuestionId, String userEmail) {

        // 사용자 추출
        User user = getUser(userEmail);

        // 아래의 검증 과정을 간소화할 순 없을까?

        // 특정 아이디의 상품 존재 여부 확인
        NSupplement supplement = nSupplementRepository.findById(nSupplementId)
                .orElseThrow(()->new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));

        // 해당 상품에 대해 nqboardId의 아이디를 갖는 문의사항이 있는지 확인
        NQuestion Nqboard = nutrientQuestionRepository.findByQuestionIdAndSupplement(nQuestionId, supplement)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        // 상품 문의사항 Id와 사용자 email로 문의사항 글 존재하는지 확인
        NQuestion nutrientQuestion = nutrientQuestionRepository.findByQuestionIdAndUser(Nqboard.getQuestionId(), user)
                .orElseThrow(()-> new NSupplementException(ExceptionMessage.PRODUCT_INQUIRY_NOT_FOUND));

        return nutrientQuestion;
    }

    // email을 바탕으로 사용자 추출
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

}
