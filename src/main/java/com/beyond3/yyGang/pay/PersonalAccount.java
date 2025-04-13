package com.beyond3.yyGang.pay;

import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "personal_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_account_id")
    private Long pId;

    private int balance;    // 잔고

    private String bankName;

    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public PersonalAccount(int balance, String bankName, User user, String accountNumber) {
        this.balance = balance;
        this.bankName = bankName;
        this.user = user;
        this.accountNumber = accountNumber;
    }

    public void decreaseBalance(int paidAmount){
        this.balance = balance - paidAmount;
    }
    public void increaseBalance(int paidAmount){
        this.balance = balance + paidAmount;
    }
}
