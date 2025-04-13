package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class NSupplementException extends BaseException {
    @Serial
    private static final long serialVersionUID = 5349366639771677547L;

    public NSupplementException(ExceptionMessage message) {
        super(message);
    }
}
