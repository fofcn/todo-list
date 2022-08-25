package com.epam.todo.common.security.jwt;

import java.security.Key;

public interface JwtSigner {

    /**
     * Sign token with header and body return base64 signature.
     * @param base64Header JWT header json encode with base64
     * @param base64Payload JWT payload json encode with base64
     * @param key encryption algorithm key
     * @return signature encode with base64
     */
    String sign(String base64Header, String base64Payload, Key key);

    /**
     * Verify signature.
     * @param base64Header JWT header json encode with base64
     * @param base64Payload JWT payload json encode with base64
     * @param base64Sign signature encode with base64
     * @param key key
     * @return true if passed verification, false otherwise.
     */
    boolean verify(String base64Header, String base64Payload, String base64Sign, Key key);
}
