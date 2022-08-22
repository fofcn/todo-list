package com.epam.common.encrypto.asymmetric;

import com.epam.common.core.lang.Assert;
import com.epam.common.encrypto.KeyUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class AbstractAsymmetricCrypto implements AsymmetricCrypto {

    private final String algorithm;

    private final PublicKey publicKey;

    private final PrivateKey privatekey;

    public AbstractAsymmetricCrypto(String algorithm) {
        this(algorithm, (byte[])null, (byte[])null);
    }

    public AbstractAsymmetricCrypto(String algorithm, String publicKey, String privateKey) {
        this(algorithm, KeyUtil.decodeHex(publicKey), KeyUtil.decodeHex(privateKey));
    }

    public AbstractAsymmetricCrypto(String algorithm, byte[] publicKey, byte[] privateKey) {
        this(algorithm,  KeyUtil.generatePublicKey(algorithm, publicKey), KeyUtil.generatePrivateKey(algorithm, privateKey));
    }

    public AbstractAsymmetricCrypto(String algorithm, PublicKey publicKey, PrivateKey privateKey) {
        this.algorithm = algorithm;
        this.publicKey = publicKey;
        this.privatekey = privateKey;
    }


    @Override
    public byte[] decrypt(byte[] bytes, KeyType keyType) {
        Assert.isNotNull(bytes, () -> new IllegalArgumentException("bytes"));
        Assert.isNotNull(keyType, () -> new IllegalArgumentException("keyType"));
        return new byte[0];
    }

    @Override
    public byte[] encrypt(byte[] bytes, KeyType keyType) {
        Assert.isNotNull(bytes, () -> new IllegalArgumentException("bytes"));
        Assert.isNotNull(keyType, () -> new IllegalArgumentException("keyType"));
        return new byte[0];
    }
}
