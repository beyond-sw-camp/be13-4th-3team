package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BoardException extends BaseException {
    @Serial
    private static final long serialVersionUID = -761677610648221189L;

    public BoardException(ExceptionMessage message) {
        super(message);
    }

}
