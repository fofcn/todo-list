package com.epam.common.encrypto.digest;

public class BcryptDigestEncoder implements DigestEncoder {

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
