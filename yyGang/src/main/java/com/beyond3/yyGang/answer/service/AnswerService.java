package com.beyond3.yyGang.answer.service;

import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.answer.dto.AnswerRequestDto;
import com.beyond3.yyGang.answer.dto.AnswerResponseDto;
import com.beyond3.yyGang.answer.repository.AnswerRepository;
import com.beyond3.yyGang.handler.exception.QuestionBoardException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.q_board.QuestionBoard;
import com.beyond3.yyGang.q_board.repository.QuestionBoardRepository;
import com.beyond3.yyGang.user.domain.Role_name;
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
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final UserRepository userRepository;


    // 영양제 관련 질문 게시판에 답글 달기 -> 이후 Dto로 응답하는게 낫지 않겠나
    @Transactional
    public AnswerResponseDto saveAnswer(Long qboardId, AnswerRequestDto requestDto, String userEmail) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 사용자가 '약사'인지 확인
        if(user.getRole() != Role_name.PHARMACIST)
        {
            throw new UserException(ExceptionMessage.ONLY_PHARMACIST_CAN_ANSWER);
        }

        // 영양제 관련 질문 게시글에 '약사'가 이미 답글을 달았는지 확인
        Optional<Answer> isExist = answerRepository.findByUserAndQboard_QboardId(user, qboardId);
        if(isExist.isPresent()){
            throw new QuestionBoardException(ExceptionMessage.QUESTION_ALREADY_ANSWERED);
        }

        // 해당 id의 게시글 찾아오기
        QuestionBoard questionBoard = questionBoardRepository.findById(qboardId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        // dto -> Entity로 변환
        Answer answer = requestDto.toEntity(questionBoard, user);

        // Entity 저장
        Answer savedAnswer = answerRepository.save(answer);

        return new AnswerResponseDto(savedAnswer);
    }

    // 답글 ID로 답글 조회 -> 단건
    @Transactional
    public AnswerResponseDto getAnswerById(Long qboardId, Long answerId) {

        // 해당 Id의 Qboard가 있는지 확인
        QuestionBoard qboards = questionBoardRepository.findById(qboardId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        // 해당 Id의 answer가 있는지 확인
        Answer answers = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_ANSWER));

        return new AnswerResponseDto(answers);
    }

    // 약 질문 ID로 전체 답글 조회
    @Transactional
    public Page<AnswerResponseDto> getAnswerByBoardId(Long qboardId, int page, int size) {

        // 입력 받은 page와 size가 유효한 값인지 아닌지 확인
        if(page < 0 || size < 0){
            throw new QuestionBoardException(ExceptionMessage.INVALID_VALUE);
        }

        Pageable pageable = PageRequest.of(page, size);

        // 해당 아이디의 질문이 있는지 확인
        QuestionBoard questionBoard =  questionBoardRepository.findById(qboardId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        // 해당 질문에 대한 답글들을 가져옴
        Page<Answer> answers = answerRepository.findByQboard_QboardId(qboardId, pageable);

        // 없으면 그냥 빈 리스트 보내기

        return answers.map(AnswerResponseDto::new);
    }


    // 답글 수정    -> 수정 수 ResponseDto로 변환해서 반환
    @Transactional
    public AnswerResponseDto updateAnswer(Long qboardId,Long answerId,AnswerRequestDto requestDto, String userEmail) {

        // 사용자 추출
        User user = getUser(userEmail);

        // Answer에 대한 수정 권한이 있는지 확인
        Answer findAnswer = validateAnswer(qboardId, answerId, user);

        // 본인이 작성한 글이 맞다면 수정 가능
        findAnswer.update(requestDto.getAnswerContent());

        return new AnswerResponseDto(findAnswer);
    }

    // 삭제 에러 로직 수정하기
    @Transactional
    public void deleteAnswer(Long qboardId, Long answerId, String userEmail) {

        // 사용자 추출
        User user = getUser(userEmail);

        // Answer에 대한 수정 권한이 있는지 확인
        Answer answer = validateAnswer(qboardId, answerId, user);

        // 본인이 작성한 글인 경우 삭제 가능
        answerRepository.delete(answer);
    }


    // answer에 대한 수정 권한이 있는지 확인
    public Answer validateAnswer(Long qboardId, Long answerId, User user) {

        // qboardId의 질문글에 대한 answerId의 아이디를 갖는 답글이 있는지?
        Answer findAnswer = answerRepository.findByAnswerIdAndQboard_QboardId(answerId, qboardId)
                .orElseThrow(()->new QuestionBoardException(ExceptionMessage.NOT_FOUND_ANSWER));

        // 만약 있다면 그게 해당 사용자가 작성한 글인지?
        if(!findAnswer.getUser().equals(user)){
            // 본인 글이 아니면 예외 던지기
            throw new QuestionBoardException(ExceptionMessage.CANNOT_EDIT_OTHER_ANSWERS);
        }

        return findAnswer;
    }


    // 이메일에서 사용자 추출
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(com.beyond3.yyGang.handler.message.ExceptionMessage.USER_NOT_FOUND));
    }
}
