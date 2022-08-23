package com.epam.common.encrypto;

import com.epam.common.core.lang.Assert;
import com.epam.common.encrypto.exception.CryptoException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Ford_Ji
 */
public class KeyUtil {

    private KeyUtil() {}

    public static KeyPair generateKeyPair(String algorithm, int keySize, SecureRandom random, AlgorithmParameterSpec... params) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            if (keySize > 0) {
                if (random != null) {
                    keyPairGenerator.initialize(keySize, random);
                } else {
                    keyPairGenerator.initialize(keySize);
                }
            }

            // 自定义初始化参数
            if (ArrayUtils.isNotEmpty(params)) {
                for (AlgorithmParameterSpec param : params) {
                    if (null == param) {
                        continue;
                    }
                    try {
                        if (null != random) {
                            keyPairGenerator.initialize(param, random);
                        } else {
                            keyPairGenerator.initialize(param);
                        }
                    } catch (InvalidAlgorithmParameterException e) {
                        throw new CryptoException(e);
                    }
                }
            }
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static byte[] decodeHex(String hexString) {
        try {
            return Hex.decodeHex(hexString);
        } catch (DecoderException e) {
            throw new CryptoException(e);
        }
    }

    public static PublicKey generatePublicKey(String algorithm, byte[] key) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        String mainAlgorithm = getAlgorithm(algorithm);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(mainAlgorithm);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        String mainAlgorithm = getAlgorithm(algorithm);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(mainAlgorithm);
            return keyFactory.generatePrivate(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

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

    private static String getAlgorithm(String algorithm) {
        final int slashIndex = algorithm.indexOf("/");
        if (slashIndex > 0) {
            return algorithm.substring(0, slashIndex);
        }
        return algorithm;
    }
}
