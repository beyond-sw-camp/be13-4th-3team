package com.beyond3.yyGang.nsupplement.dto;



import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;

import java.util.Arrays;

public class EnumUtil {
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new UserException(ExceptionMessage.INVALID_VALUE));
    }
}
