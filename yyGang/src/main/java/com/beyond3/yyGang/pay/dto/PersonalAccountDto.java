package com.beyond3.yyGang.pay.dto;

import com.beyond3.yyGang.pay.PersonalAccount;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PersonalAccountDto {

    @NotBlank(message = "은행 이름 입력은 필수 입니다.")
    private String bankName;

    @NotBlank(message = "계좌번호 입력은 필수 입니다.")
    private String accountNumber;   // 계좌 번호

    @PositiveOrZero // 음수 잔고는 없으니까
    private int balance;    // 잔고

    public PersonalAccount toEntity(User user) {
        return PersonalAccount.builder()
                .bankName(bankName)
                .balance(balance)
                .accountNumber(accountNumber)
                .user(user)
                .build();
    }
}
