package com.epam.common.encrypto.asymmetric;

public class AbstractAsymmetricCrypto implements AsymmetricCrypto {
    @Override
    public byte[] decrypt(byte[] bytes, KeyType keyType) {
        return new byte[0];
    }

    @Override
    public byte[] encrypt(byte[] bytes, KeyType keyType) {
        return new byte[0];
    }
}
