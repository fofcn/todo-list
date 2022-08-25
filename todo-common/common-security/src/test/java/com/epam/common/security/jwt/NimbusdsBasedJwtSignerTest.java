package com.epam.common.security.jwt;

import com.epam.todo.common.security.jwt.JwtException;
import com.epam.todo.common.security.jwt.JwtSigner;
import com.epam.todo.common.security.jwt.NimbusdsBasedJwtSigner;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NimbusdsBasedJwtSignerTest {

    private String mockedInvalidHeader = RandomStringUtils.randomAlphabetic(32);

    private String mockedInvalidPayload = RandomStringUtils.randomAlphabetic(64);

    private static String validBase64Header;

    public static String validBase64Payload;

    private static String validBase64Sign;

    private static KeyPair keyPair;

    @BeforeAll
    public static void beforeAll() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjEzNTAxNDYsInVzZXJJZCI6MiwianRpIjoiOHpLeVU0UkUwRm9mSDJTSGI2OWtGWHdkaG9JIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.ENPZYbzpCHaL9QS5qUVSZOqMU1Mj0u7rAP6zVuohvhrqHpFxlrDySshpj-hsB8NrWjpH4VC_aFd8Iz9OD3UUGNLQ6HRMXykjbJbwUqvuf8NrEN62wovmx0XwNmyUXGlERvQvns1JGKo9elK2z-dzww-4_B11aJ88v0UyNwyFh-dyDD7PfBX6vEJQITpzGEcuYodlTQONJfrO2QPW41UooYCXtT0DTcu3mHf51ppFLfC7yg-haEOyPbaqSE22OclkjyD6vw4vhgbQhYD2szTXbitxA91VkmBwNYL4p9HuVMPbntkfIKa6P12Np1i0j-2Qld54InfZEv_rnV7jZd2B-Q";
        // load keystore from jks file
        final KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(NimbusdsBasedJwtSignerTest.class.getResourceAsStream("/keystore.jks"), "mypass".toCharArray());
        final PrivateKey privateKey = (PrivateKey) keyStore.getKey("servercert", "mypass".toCharArray());
        final Certificate cert = keyStore.getCertificate("servercert");
        final PublicKey publicKey = cert.getPublicKey();
        keyPair = new KeyPair(publicKey, privateKey);

        String[] tokenParts = token.split("\\.");
        validBase64Header = tokenParts[0];
        validBase64Payload = tokenParts[1];
        validBase64Sign = tokenParts[2];
    }

    @Test
    void testSignShouldReturnValidSign() {
        JwtSigner jwtSigner = createValidSigner();
        String signature = jwtSigner.sign(validBase64Header, validBase64Payload, keyPair.getPrivate());
        assertNotNull(signature);
    }

    @Test
    void testSignShouldReturnJwtException() {
        JwtSigner jwtSigner = createValidSigner();
        PrivateKey privateKey = keyPair.getPrivate();
        assertThrows(JwtException.class, () -> jwtSigner.sign(mockedInvalidHeader, mockedInvalidPayload, privateKey));
    }

    @Test
    void testVerifySignShouldReturnTrue() {
        JwtSigner jwtSigner = createValidSigner();
        boolean isCorrect = jwtSigner.verify(validBase64Header, validBase64Payload, validBase64Sign, keyPair.getPublic());
        assertTrue(isCorrect);
    }

    @Test
    void testVerifySignShouldReturnFalse() {
        JwtSigner jwtSigner = createValidSigner();
        boolean isCorrect = jwtSigner.verify(validBase64Header, mockedInvalidPayload, validBase64Sign, keyPair.getPublic());
        assertFalse(isCorrect);
    }

    private JwtSigner createValidSigner() {
        return new NimbusdsBasedJwtSigner();
    }

    private String createValidSign() {
        JwtSigner jwtSigner = createValidSigner();
        return jwtSigner.sign(validBase64Header, validBase64Payload, keyPair.getPrivate());
    }

    private String createInvalidSign() {
        return RandomStringUtils.randomAlphabetic(12);
    }
}
