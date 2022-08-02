package com.epam.common.encrypto.digest;

public class HMACDigestEncoder implements DigestEncoder {

    public final String algorithm;

    public HMACDigestEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String encode(String plain) {
        return null;
    }

    @Override
    public String encode(String plain, String salt) {
        return null;
    }

    @Override
    public boolean matches(String plain, String encode) {
        return false;
    }
}
