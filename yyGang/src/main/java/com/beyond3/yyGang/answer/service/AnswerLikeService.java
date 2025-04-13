package com.beyond3.yyGang.answer.service;

import com.beyond3.yyGang.answer.domain.Answer;
import com.beyond3.yyGang.answer.domain.AnswerLike;
import com.beyond3.yyGang.answer.domain.AnswerLikedId;
import com.beyond3.yyGang.answer.repository.AnswerLikeRepository;
import com.beyond3.yyGang.answer.repository.AnswerRepository;
import com.beyond3.yyGang.handler.exception.QuestionBoardException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import com.beyond3.yyGang.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerLikeService {

    private final AnswerLikeRepository likeRepository;
    private final AnswerRepository answerRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AnswerLikeRepository answerLikeRepository;

    @Transactional
    public void likeAnswer(Long answerId, String userEmail) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 해당 Id의 answer가 있는지 확인
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_ANSWER));

        Optional<AnswerLike> userAnswerLike = answerLikeRepository.findByUserAndAnswer(user, answer);

        if(userAnswerLike.isPresent()) {
            // 값이 존재하는 경우 -> 이미 좋아요를 누른 경우
            throw new QuestionBoardException(ExceptionMessage.ANSWER_ALREADY_LIKED);
        }

        // 복합키 우선 생성
        AnswerLikedId likedId = new AnswerLikedId(user.getUserId(), answerId);

        // AnswerLike 객체 생성
        AnswerLike answerLike = new AnswerLike(likedId, user, answer);

        answerLikeRepository.save(answerLike);
    }

    // 좋아요 취소
    @Transactional
    public void unLikeAnswer(Long answerId, String userEmail) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 해당 Id의 answer가 있는지 확인
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_ANSWER));

        // 사용자가 좋아요를 눌렀는지 아닌지 확인
        AnswerLike answerLike = answerLikeRepository.findByUserAndAnswer(user, answer)
                .orElseThrow(()-> new QuestionBoardException(ExceptionMessage.ANSWER_NOT_LIKED));

        // 누른 적 없는 경우 삭제
        answerLikeRepository.delete(answerLike);
    }

    // 이메일에서 사용자 추출
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(com.beyond3.yyGang.handler.message.ExceptionMessage.USER_NOT_FOUND));
    }
}
