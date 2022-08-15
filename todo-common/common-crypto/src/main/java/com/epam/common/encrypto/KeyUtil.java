package com.epam.common.encrypto;

import com.epam.common.core.lang.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Ford_Ji
 */
public class KeyUtil {

    private KeyUtil() {}

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     * 参考hutool
     * @param algorithm 算法
     * @param key       密钥，如果为{@code null} 自动生成随机密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, byte[] key) {
        Assert.isNotEmpty(algorithm, () -> new IllegalArgumentException("Algorithm is blank"));
        SecretKey secretKey = (null == key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
        return secretKey;
    }

    /**
     * Get algorithm's key genenrator
     * @return algorithm
     */
    public static KeyGenerator getKeyGenerator(String algorithm) {
        try {
            return KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such key generator of algorithm: " + algorithm, e);
        }
    }

    public static SecretKeyFactory getSecretKeyFactory(String algorithm) {
        try {
            return SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such key secret key factory of algorithm: " + algorithm, e);
        }
    }

    public static SecretKey generateKey(String algorithm) {
        return generateKey(algorithm, -1);
    }

    public static SecretKey generateKey(String algorithm, int keySize) {
        return generateKey(algorithm, keySize, null);
    }

    public static SecretKey generateKey(String algorithm, int keySize, SecureRandom random) {
        final KeyGenerator keyGenerator = getKeyGenerator(algorithm);

        if (keySize > 0) {
            if (random == null) {
                keyGenerator.init(keySize);
            } else {
                keyGenerator.init(keySize, random);
            }
        }

        return keyGenerator.generateKey();
    }
}
