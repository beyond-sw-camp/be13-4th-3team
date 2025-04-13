package com.beyond3.yyGang.q_board.service;

import com.beyond3.yyGang.handler.exception.QuestionBoardException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.q_board.dto.QboardPageResponseDto;
import com.beyond3.yyGang.q_board.dto.QboardRequestDto;
import com.beyond3.yyGang.q_board.dto.QboardResponseDto;
import com.beyond3.yyGang.q_board.dto.QboardUpdateDto;
import com.beyond3.yyGang.q_board.QuestionBoard;
import com.beyond3.yyGang.q_board.repository.QuestionBoardRepository;
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
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final UserRepository userRepository;

//    public static ResponseEntity<List<QboardResponseDto>> getQboard(int page, String criteria) {
//        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, criteria));
//        Page<QboardResponseDto> page = questionBoardRepository.findAll(pageable).map(QboardResponseDto::new);
//        return ResponseEntity.ok(page.getContent());
//    }         -> ?

    // 게시글 저장  -> 저장 후 엔티티 보이도록
    @Transactional
    public QboardResponseDto saveQboard(QboardRequestDto requestDto, String userEmail) {

        // 사용자 존재하는지 확인
        User user = getUser(userEmail);

        // dto를 Entity로 변환 후 저장
        QuestionBoard qboard = requestDto.toEntity(user);
        QuestionBoard save = questionBoardRepository.save(qboard);

        return new QboardResponseDto(save);
    }

    // 전체 게시글 조회
    @Transactional
    public QboardPageResponseDto getAllQboard(int page, int size) {

        // 입력 받은 page와 size가 유효한 값인지 아닌지 확인
        if(page < 0 || size <= 0){
            throw new QuestionBoardException(ExceptionMessage.INVALID_VALUE);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<QuestionBoard> qboardpage = questionBoardRepository.findAll(pageable);

        // qboardpage 존재하지 않는 경우
        if (qboardpage.isEmpty()) {
            throw new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD);
        }

        List<QboardResponseDto> qboardResponseDtos = qboardpage.stream().map(QboardResponseDto::new).toList();

        QboardPageResponseDto qboardPageResponseDto = new QboardPageResponseDto(qboardResponseDtos,qboardpage.getTotalElements());

        // responseDto로 변환해서 return
        return qboardPageResponseDto;
    }


    // 특정 ID로 게시글 조회
    @Transactional
    public QboardResponseDto getQboardById(Long qboardId) {

        // qboardId로 게시글 찾기 -> 없으면 예외 던지기
        QuestionBoard questionBoard =  questionBoardRepository.findById(qboardId)
                .orElseThrow(() -> new QuestionBoardException(ExceptionMessage.NOT_FOUND_QUESTION_BOARD));

        // 조회수 증가
        questionBoardRepository.updateViewCount(qboardId);

        return new QboardResponseDto(questionBoard);
    }


    // 특정 게시글 수정
    @Transactional
    public QboardResponseDto updateQboard(Long qboardId, QboardUpdateDto requestDto, String userEmail) {

        // 사용자 확인
        User user = getUser(userEmail);

        // 해당 user가 작성한 글 중 해당 id의 글이 있는지 확인
        QuestionBoard questionBoard = questionBoardRepository.findByUserAndQboardId(user, qboardId)
                .orElseThrow(()-> new QuestionBoardException(ExceptionMessage.CANNOT_EDIT_CONTENTS));

        // 게시글 수정 시 수정사항이 있는 경우만 update 하도록
        questionBoard.update(requestDto.getTitle(), requestDto.getContent());

        // 수정 결과 반환하도록
        return new QboardResponseDto(questionBoard);
    }

    // 게시글 삭제
    @Transactional
    public void deleteQboard(Long qboardId, String userEmail) {

        // 사용자 추출
        User user = getUser(userEmail);

        // 사용자가 작성한 해당 id의 게시글이 있는지 확인
        QuestionBoard questionBoard = questionBoardRepository.findByUserAndQboardId(user, qboardId)
                .orElseThrow(()-> new QuestionBoardException(ExceptionMessage.CANNOT_EDIT_CONTENTS));

        // 게시글 삭제
        questionBoardRepository.delete(questionBoard);
    }

    // 해당 email을 갖는 사용자가 존재하는지 확인
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(com.beyond3.yyGang.handler.message.ExceptionMessage.USER_NOT_FOUND));
    }
}
