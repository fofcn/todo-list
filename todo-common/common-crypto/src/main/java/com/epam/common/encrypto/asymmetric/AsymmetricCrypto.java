package com.epam.common.encrypto.asymmetric;

public interface AsymmetricCrypto {

    byte[] encrypt(byte[] bytes, KeyType keyType);

    byte[] decrypt(byte[] bytes, KeyType keyType);
}
