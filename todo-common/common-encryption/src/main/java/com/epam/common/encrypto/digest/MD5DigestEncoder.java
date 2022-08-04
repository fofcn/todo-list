package com.epam.common.encrypto.digest;

public class MD5DigestEncoder implements DigestEncoder {
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

    @Override
    public boolean matches(String plain, String salt, String encode) {
        return false;
    }
}
