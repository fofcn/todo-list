package com.epam.common.encrypto.symmetric;

import com.epam.common.encrypto.KeyUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class AbstractSymmetricCrypto implements SymmetricCrypto {

    private final SymmetricAlgorithm algorithm;

    private SecretKey secretKey;

    private Cipher cipher;

    private AlgorithmParameterSpec parameterSpec;

    private SecureRandom random;

    private boolean isZeroPadding;

    public AbstractSymmetricCrypto(SymmetricAlgorithm algorithm) {
        this(algorithm, (byte[])null);
    }

    public AbstractSymmetricCrypto(SymmetricAlgorithm algorithm, byte[] key) {
        this(algorithm, KeyUtil.generateKey(algorithm.getValue(), key));
    }

    public AbstractSymmetricCrypto(SymmetricAlgorithm algorithm, SecretKey secretKey) {
        this(algorithm, secretKey, null);
    }

    public AbstractSymmetricCrypto(SymmetricAlgorithm algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
        this.algorithm = algorithm;
        try {
            this.cipher = Cipher.getInstance(algorithm.getValue());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }

        if (null == paramsSpec) {
            byte[] iv = cipher.getIV();
            this.parameterSpec = new IvParameterSpec(iv);
        }

    }

    @Override
    public byte[] encrypt(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return new byte[0];
    }
}
