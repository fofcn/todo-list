package com.epam.common.encrypto.symmetric;

public class AbstractSymmetricCrypto implements SymmetricCrypto {
    @Override
    public byte[] encrypt(byte[] bytes) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return new byte[0];
    }
}
