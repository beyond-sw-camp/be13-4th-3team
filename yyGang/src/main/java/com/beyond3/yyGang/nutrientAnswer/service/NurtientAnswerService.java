package com.beyond3.yyGang.nutrientAnswer.service;

import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.handler.exception.NAnswerException;
import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nutrientAnswer.NAnswer;
import com.beyond3.yyGang.nutrientAnswer.dto.NurtientAnswerRequestDto;
import com.beyond3.yyGang.nutrientAnswer.dto.NutrientAnswerModifyDto;
import com.beyond3.yyGang.nutrientAnswer.dto.NutrientAnswerResponseDto;
import com.beyond3.yyGang.nutrientAnswer.repository.NurtientAnswerRepository;
import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.nutrientQuestion.repository.NutrientQuestionRepository;
import com.beyond3.yyGang.user.domain.Role_name;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NurtientAnswerService {

    private final NutrientQuestionRepository nutrientQuestionRepository;
    private final NSupplementRepository nSupplementRepository;
    private final UserRepository userRepository;
    private final NurtientAnswerRepository nurtientAnswerRepository;

    // 답글 작성
    @Transactional
    public NutrientAnswerResponseDto saveAnswer(Long nSupplementId, Long nqboardId, NurtientAnswerRequestDto requestDto, String userEmail) {

        // 사용자 추출
        User user = getUser(userEmail);

        // 이 과정을 패치 조인 하나로 확인하면 어떨까?
//        // 해당 Id의 상품에 대한 작성자가 사용자인지 확인
//        NSupplement product = nSupplementRepository.verifySeller(user.getEmail(), nSupplementId)
//                .orElseThrow(() -> new NSupplementException(ExceptionMessage.NO_REGISTERED_PRODUCTS));
//
//        // 해당 Id의 질문글이 해당 상품에 대한 것인지 확인
//        NQuestion nqboard = nutrientQuestionRepository.findByQuestionIdAndSupplement(nqboardId, product)
//                .orElseThrow(() -> new NSupplementException(ExceptionMessage.PRODUCT_INQUIRY_NOT_FOUND));

        // 해당 Id를 갖는 질문 글의 상품 Id와 상품 등록자가 일치하는지 확인 -> 패치 조인 이용
        NQuestion nQuestion = nutrientQuestionRepository.verifySellerAndNQboard(user.getEmail(), nSupplementId, nqboardId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.PRODUCT_INQUIRY_NOT_FOUND));

        // 혹시 상품 질문에 대한 답변 등록을 이미 했는지?
        Optional<NAnswer> nAnswer = nurtientAnswerRepository.verifyNAnswer(user.getEmail(), nQuestion.getQuestionId());

        // 이미 질문에 대해 답변을 작성한 적이 있는 경우
        if(nAnswer.isPresent()) {
            throw new NAnswerException(ExceptionMessage.QUESTION_ALREADY_ANSWERED);
        }

        // 다 맞으면 답글 작성 가능
        NAnswer resultAnswer = requestDto.toEntity(user, nQuestion);
        NAnswer save = nurtientAnswerRepository.save(resultAnswer);

        return new NutrientAnswerResponseDto(save);
    }

    // 답글 조회 -> 특정 Id 의 답글을 찾음
    @Transactional
    public NutrientAnswerResponseDto getAnswer(Long nSupplementId, Long nqboardId, Long answerId) {

        // 해당 상품이 존재하는지 확인
        NSupplement byproductId = nSupplementRepository.findByproductId(nSupplementId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.PRODUCT_NOT_FOUND));

        // 해당 상품의 Id를 갖는 상품이 nqboardId의 answer를 가지고 있는지 확인
        NAnswer nAnswer = nurtientAnswerRepository.getAnswer(nSupplementId, nqboardId, answerId)
                .orElseThrow(() -> new NAnswerException(ExceptionMessage.CANNOT_FOUND_ANSWER));

        return new NutrientAnswerResponseDto(nAnswer);
    }

    // 특정 문의사항에 대한 모든 답변 조회
    @Transactional
    public NutrientAnswerResponseDto getAllNAnswer(Long nSupplementId, Long nqboardId) {

//        // 페이징 처리를 위해 받은 값이 유효한지 확인
//        if(page < 0 || size <= 0){
//            throw new NAnswerException(ExceptionMessage.INVALID_VALUE);
//        }
//
//        Pageable pageable = PageRequest.of(page, size);

        // 해당 상품이 존재하는지 확인
        NSupplement byproductId = nSupplementRepository.findByproductId(nSupplementId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.PRODUCT_NOT_FOUND));

        NAnswer allAnswer = nurtientAnswerRepository.getAllAnswer(nSupplementId, nqboardId)
                .orElseThrow(() -> new NAnswerException(ExceptionMessage.NOT_FOUND_ANSWER));

        return new NutrientAnswerResponseDto(allAnswer);
    }


    // 답글 수정
    @Transactional
    public NutrientAnswerResponseDto updateAnswer(String userEmail, Long nSupplementId, Long nQboardId, Long nAnswerId, NutrientAnswerModifyDto requestDto){

        // 답변에 대한 검증 로직
        NAnswer nAnswer = verifyNAnswer(userEmail, nSupplementId, nQboardId, nAnswerId);

        // 답변 수정
        nAnswer.update(requestDto);

        return new NutrientAnswerResponseDto(nAnswer);
    }

    // 답글 삭제
    @Transactional
    public void deleteAnswer(String userEmail, Long nSupplementId, Long nQboardId, Long nAnswerId, NutrientAnswerModifyDto requestDto) {

        // 답변에 대한 검증 로직
        NAnswer nAnswer = verifyNAnswer(userEmail, nSupplementId, nQboardId, nAnswerId);

        // 답변 삭제
        nurtientAnswerRepository.delete(nAnswer);
    }

    // 해당 질의 응답에 대해 답변을 수정 또는 삭제할 권한을 가지고 있는지 확인
    public NAnswer verifyNAnswer(String userEmail, Long nSupplementId, Long nQboardId, Long nAnswerId) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 해당 문의사항에 대한 답변 확인
        NAnswer answer = nurtientAnswerRepository.getAnswer(nSupplementId, nQboardId, nAnswerId)
                .orElseThrow(() -> new NAnswerException(ExceptionMessage.CANNOT_FOUND_ANSWER));

        // 해당 답변의 작성자가 사용자가 맞는지 확인
        // 사용자의 역할이 SELLER가 아니거나, answer의 작성자가 사용자가 아닌 경우
        if(!user.getRole().equals(Role_name.SELLER) || !answer.getUser().equals(user)) {
            throw new NAnswerException(ExceptionMessage.CANNOT_EDIT_OTHER_ANSWERS);
        }

        return answer;
    }

    // email을 바탕으로 사용자 추출
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}


