package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class CartEntityException extends BaseException {

    @Serial
    private static final long serialVersionUID = -556170430237210994L;

    public CartEntityException(ExceptionMessage message) {
        super(message);
    }
}
