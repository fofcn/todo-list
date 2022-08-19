package com.epam.common.encrypto.asymmetric;

import com.epam.common.core.lang.Assert;

public class AbstractAsymmetricCrypto implements AsymmetricCrypto {
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
