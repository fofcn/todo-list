package com.epam.todo.auth.common;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexPatternTest {

    @Test
    public void testMatch() {
//        String exp = "(?=.\\d)(?=.[a-z])(?=.[A-Z])((?=.\\W)|(?=.*_))^[^ ]+$";
        String exp = "[A-z\\d]{8,20}";
        Pattern p = Pattern.compile(exp);
        Matcher matcher = p.matcher("aa123456");

        assertTrue(matcher.matches());
    }
}
