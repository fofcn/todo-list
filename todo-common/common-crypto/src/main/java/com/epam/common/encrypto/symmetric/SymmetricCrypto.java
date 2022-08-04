package com.epam.common.encrypto.symmetric;

public interface SymmetricCrypto {

    byte[] encrypt(byte[] bytes);

    byte[] decrypt(byte[] bytes);
}
