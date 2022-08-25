package com.epam.common.security.jwt;

import com.epam.common.encrypto.KeyUtil;
import com.epam.common.encrypto.asymmetric.AsymmetricAlgorithm;
import com.epam.todo.common.security.jwt.Jwt;
import com.epam.todo.common.security.jwt.JwtException;
import jodd.util.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTest {

    private static String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA87tXu17hK91+ItLkb4F4" +
            "TgL0LceSncdMFxEDYq7wOTqubH6n2zeW8b2ZI01vUrO6W+xmuGaPW4Kgdh/O1KM5" +
            "rapgS9ujSIES1Y/CDqQG3M4nVdGTVvYPimKKT4F/etSXkuBLkcIYDR2IpfQQ3GTr" +
            "tuO6IK11/9yjaL6G3bWWCpfkH9ydW875x6XTy7JL7gPi3pElVZYvz8VWxOroW9zS" +
            "VA8nkLE4WQ+rfP68m+0SdH1TN3f0X+UNbTdrLpqIy8UuwCLnMOdCfmpi2qM0dKb/" +
            "s1bTCh4/Nhzhlixs8i5Un5xor2dTN0HKoqEnQBN+VJwdLz7QLyIFDAfVGxeUzGlp" +
            "mQIDAQAB";

    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjEzNTAxNDYsInVzZXJJZCI6MiwianRpIjoiOHpLeVU0UkUwRm9mSDJTSGI2OWtGWHdkaG9JIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.ENPZYbzpCHaL9QS5qUVSZOqMU1Mj0u7rAP6zVuohvhrqHpFxlrDySshpj-hsB8NrWjpH4VC_aFd8Iz9OD3UUGNLQ6HRMXykjbJbwUqvuf8NrEN62wovmx0XwNmyUXGlERvQvns1JGKo9elK2z-dzww-4_B11aJ88v0UyNwyFh-dyDD7PfBX6vEJQITpzGEcuYodlTQONJfrO2QPW41UooYCXtT0DTcu3mHf51ppFLfC7yg-haEOyPbaqSE22OclkjyD6vw4vhgbQhYD2szTXbitxA91VkmBwNYL4p9HuVMPbntkfIKa6P12Np1i0j-2Qld54InfZEv_rnV7jZd2B-Q";
    private static PublicKey publicKey;

    private final long validateSeconds = 7200L;

    @BeforeAll
    static void beforeAll() {
        publicKey = KeyUtil.generatePublicKey(AsymmetricAlgorithm.RSA.getValue(), Base64.decode(publicKeyString));
    }

    @Test
    void testOfGivenValidParamsThenReturnCorrectJwtObject() {
        Jwt jwt = Jwt.of(token, validateSeconds);
        assertNotNull(jwt);
    }

    @Test
    void testOfInvalidParamsThenReturnJwtException() {
        String invalidToken = token.split("\\.")[0];
        assertThrows(JwtException.class, () -> Jwt.of(invalidToken, validateSeconds));
    }

    @Test
    void testVerifySignShouldReturnTrue() {
        Jwt jwt = Jwt.of(token, validateSeconds);
        boolean isSignCorrect = jwt.verifySign(publicKey);
        assertTrue(isSignCorrect);
    }

    @Test
    void testVerifySignShouldReturnFalse() {
        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue(), 2048, null, null);
        Jwt jwt = Jwt.of(token);
        boolean isSignCorrect = jwt.verifySign(keyPair.getPublic());
        assertFalse(isSignCorrect);
    }
}
