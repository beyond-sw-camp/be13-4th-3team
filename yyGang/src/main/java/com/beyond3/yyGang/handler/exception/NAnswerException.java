package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class NAnswerException extends BaseException {
    @Serial
    private static final long serialVersionUID = -6547589487626372738L;

    public NAnswerException(ExceptionMessage message) {
        super(message);
    }
}
