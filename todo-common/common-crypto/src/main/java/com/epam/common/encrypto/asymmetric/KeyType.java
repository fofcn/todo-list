package com.epam.common.encrypto.asymmetric;

import javax.crypto.Cipher;

public enum KeyType {

    PUBLIC_KEY(Cipher.PUBLIC_KEY),
    PRIVATE_KEY(Cipher.PRIVATE_KEY),
    SECRET_KEY(Cipher.SECRET_KEY);

    private final int value;

    KeyType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
