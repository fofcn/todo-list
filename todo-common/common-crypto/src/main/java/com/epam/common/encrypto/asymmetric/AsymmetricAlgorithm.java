package com.epam.common.encrypto.asymmetric;

/**
 * see: Hutool AsymmetricAlgorithm.java
 */
public enum AsymmetricAlgorithm {

    RSA("RSA"),
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),
    RSA_ECB("RSA/ECB/NoPadding"),
    RSA_None("RSA/None/NoPadding");

    private final String value;

    AsymmetricAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
