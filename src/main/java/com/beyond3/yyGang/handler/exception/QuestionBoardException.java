package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;

import java.io.Serial;

public class QuestionBoardException extends BaseException {

    @Serial
    private static final long serialVersionUID = 94449686677402437L;

    public QuestionBoardException(ExceptionMessage message) {
        super(message);
    }
}
