package com.epam.common.encrypto.symmetric;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface SymmetricCrypto {

    /**
     * encryption with bytes and return bytes data
     * @param data byte array to be encrypted
     * @return encrypted bytes
     */
    byte[] encrypt(byte[] data);

    /**
     * encryption with string data and return string data.
     * @param data string data to be encrypted default encoding utf-8
     * @return encrypted string data
     */
    default String encrypt(String data) {
       byte[] bytes = encrypt(data, StandardCharsets.UTF_8);
       return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param data string data to be encrypted
     * @param charset data character encoding
     * @return encrypted byte array
     */
    default byte[] encrypt(String data, Charset charset) {
        return encrypt(data.getBytes(charset));
    }

    /**
     * encryption with bytes and return string data
     * @param data byte array to be encrypted default encoding utf-8
     * @return encrypted string data
     */
    default String encryptHex(byte[] data) {
        byte[] bytes = encrypt(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * decrypt encrypted data to byte array
     * @param data encrypted data array
     * @return
     */
    byte[] decrypt(byte[] data);

    /**
     * decrypt encrypted data to string
     * @param data encrypted string data
     * @return decrypted string data
     */
    default String decryptStr(String data) {
        return decryptStr(data, StandardCharsets.UTF_8);
    }

    /**
     * decrypt encrypted data to string
     * @param data encrypted string data
     * @param charset data character encoding
     * @return decrypted string data
     */
    default String decryptStr(String data, Charset charset) {
        byte[] bytes = decrypt(data.getBytes(charset));
        return new String(bytes, charset);
    }
}
