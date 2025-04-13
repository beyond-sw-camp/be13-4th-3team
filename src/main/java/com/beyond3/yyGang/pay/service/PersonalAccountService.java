package com.beyond3.yyGang.pay.service;

import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.pay.PersonalAccount;
import com.beyond3.yyGang.pay.dto.PersonalAccountDto;
import com.beyond3.yyGang.pay.repository.PersonalAccountRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PersonalAccountService {

    private final UserRepository userRepository;
    private final PersonalAccountRepository personalAccountRepository;


    @Transactional
    // 개인 회원 계좌 등록  -> 계좌는 인 당 하나만 만들도록
    public void personalAccountRegister(String email, PersonalAccountDto personalAccountDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        validatePersonalAccount(user);

        PersonalAccount personalAccount = personalAccountDto.toEntity(user);

        personalAccountRepository.save(personalAccount);
    }

    @Transactional
    // 개인 계좌 정보 삭제
    public void personalAccountDelete(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        Optional<PersonalAccount> personalAccount = personalAccountRepository.findByUserId(user.getUserId());

        if(personalAccount.isEmpty()){
            throw new UserException(ExceptionMessage.ACCOUNT_NOT_EXIST);
        }
        personalAccountRepository.delete(personalAccount.get());
    }

    // 계좌 유효성 확인
    public void validatePersonalAccount(User user){
        Optional<PersonalAccount> personalAccount = personalAccountRepository.findByUserId(user.getUserId());

        if(personalAccount.isPresent()){
            // 계좌가 이미 존재하면 예외 던지기
            throw new UserException(ExceptionMessage.ACCOUNT_EXIST);
        }
    }
}
