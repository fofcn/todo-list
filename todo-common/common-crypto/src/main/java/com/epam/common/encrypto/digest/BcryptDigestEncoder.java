package com.epam.common.encrypto.digest;

import com.epam.common.core.lang.Assert;

public class BcryptDigestEncoder implements DigestEncoder {

    @Override
    public String encode(String plain) {
        Assert.isNotEmpty(plain, () -> new IllegalArgumentException());
        return BCrypt.hashpw(plain);
    }

    @Override
    public String encode(String plain, String salt) {
        Assert.isNotEmpty(plain, () -> new IllegalArgumentException());
        Assert.isNotEmpty(salt, () -> new IllegalArgumentException());
        return BCrypt.hashpw(plain, salt);
    }

    @Override
    public boolean matches(String plain, String encode) {
        Assert.isNotEmpty(plain, () -> new IllegalArgumentException());
        Assert.isNotEmpty(encode, () -> new IllegalArgumentException());
        return BCrypt.checkpw(plain, encode);
    }

    @Override
    public boolean matches(String plain, String salt, String encode) {
        Assert.isNotEmpty(plain, () -> new IllegalArgumentException());
        Assert.isNotEmpty(salt, () -> new IllegalArgumentException());
        Assert.isNotEmpty(encode, () -> new IllegalArgumentException());
        return BCrypt.checkpw(plain, encode);
    }
}
