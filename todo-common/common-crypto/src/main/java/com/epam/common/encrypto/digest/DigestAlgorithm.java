package com.epam.common.encrypto.digest;

import lombok.Getter;

@Getter
public enum DigestAlgorithm {
    ALG_MD5("HmacMD5"),

    ALG_SHA1("HmacSHA1"),

    ALG_SHA256("HmacSHA256"),

    ALG_SHA384("HmacSHA384"),

    ALG_SHA512("HmacSHA512"),

    ALG_SM3("HmacSM3"),

    ALG_SM4("SM4CMAC");

    private final String value;


    DigestAlgorithm(final String value) {
        this.value = value;
    }


}
