package com.epam.common.security.jwt;

import com.epam.todo.common.security.jwt.JwtException;
import com.epam.todo.common.security.jwt.JwtPayload;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtPayloadTest {

    private static String validBase64Payload;

    @BeforeAll
    public static void beforeAll() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjEzNTAxNDYsInVzZXJJZCI6MiwianRpIjoiOHpLeVU0UkUwRm9mSDJTSGI2OWtGWHdkaG9JIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.ENPZYbzpCHaL9QS5qUVSZOqMU1Mj0u7rAP6zVuohvhrqHpFxlrDySshpj-hsB8NrWjpH4VC_aFd8Iz9OD3UUGNLQ6HRMXykjbJbwUqvuf8NrEN62wovmx0XwNmyUXGlERvQvns1JGKo9elK2z-dzww-4_B11aJ88v0UyNwyFh-dyDD7PfBX6vEJQITpzGEcuYodlTQONJfrO2QPW41UooYCXtT0DTcu3mHf51ppFLfC7yg-haEOyPbaqSE22OclkjyD6vw4vhgbQhYD2szTXbitxA91VkmBwNYL4p9HuVMPbntkfIKa6P12Np1i0j-2Qld54InfZEv_rnV7jZd2B-Q";
        String[] tokenParts = token.split("\\.");
        validBase64Payload = tokenParts[1];
    }

    @Test
    void testParseShouldReturnSuccess() {
        JwtPayload jwtPayload = new JwtPayload();
        jwtPayload.parse(validBase64Payload);
        assertEquals("errorfatal89@gmail.com", jwtPayload.get("username").toString());
    }

    @Test
    void testParseShouldThrowsJwtException() {
        String invalidBas64Payload = RandomStringUtils.randomAlphabetic(64);
        JwtPayload jwtPayload = new JwtPayload();
        assertThrows(JwtException.class, () -> jwtPayload.parse(invalidBas64Payload));
    }

    @Test
    void testGetExpireDateShouldReturnSuccess() {
        JwtPayload jwtPayload = new JwtPayload();
        jwtPayload.parse(validBase64Payload);
        LocalDateTime expireDate = jwtPayload.getExpireDate();
        LocalDateTime expectDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(1661350146L),
                TimeZone.getDefault().toZoneId());
        assertEquals(expectDate, expireDate);
    }
}
