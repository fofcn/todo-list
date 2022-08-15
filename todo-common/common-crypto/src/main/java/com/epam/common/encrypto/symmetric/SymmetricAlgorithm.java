package com.epam.common.encrypto.symmetric;

public enum SymmetricAlgorithm {
    AES("AES");


    private final String value;

    SymmetricAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
