package com.beyond3.yyGang.handler.exception;

import com.beyond3.yyGang.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class OrderException extends BaseException {

  @Serial
  private static final long serialVersionUID = -3325163702920695888L;

  public OrderException(ExceptionMessage message) {
      super(message);
  }
}
