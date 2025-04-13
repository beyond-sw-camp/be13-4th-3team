package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {

      @Serial
      private static final long serialVersionUID = 3288379394474107012L;

      private final String type;
      private final HttpStatus status;

      protected BaseException(ExceptionMessage message) {
        super(message.getMessage());
        this.type = message.name();
        this.status = message.getStatus();
      }
}
