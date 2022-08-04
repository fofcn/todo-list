package com.epam.common.encrypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Ford_Ji
 */
public class KeyUtil {

    private KeyUtil() {}

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
