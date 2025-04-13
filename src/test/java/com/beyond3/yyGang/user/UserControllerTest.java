package com.beyond3.yyGang.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void dbConnectionTest(){
        Assertions.assertThat(true).isTrue();
    }

}