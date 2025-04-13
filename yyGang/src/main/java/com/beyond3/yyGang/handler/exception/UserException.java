package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class UserException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1104867475557607042L;

    public UserException(ExceptionMessage message) {
        super(message);
    }
}
