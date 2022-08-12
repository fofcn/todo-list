package com.epam.common.encrypto.digest;

import com.epam.common.core.lang.Assert;
import org.apache.commons.lang3.StringUtils;

public class MD5DigestEncoder implements DigestEncoder {
    @Override
    public String encode(String plain) {
        Assert.isNotEmpty(plain, IllegalArgumentException::new);
        return Md5Util.md5Hex(plain, StringUtils.EMPTY);
    }

    @Override
    public String encode(String plain, String salt) {
        Assert.isNotEmpty(plain, IllegalArgumentException::new);
        Assert.isNotEmpty(salt, IllegalArgumentException::new);
        return Md5Util.md5Hex(plain, salt);
    }

    @Override
    public boolean matches(String plain, String encode) {
        Assert.isNotEmpty(plain, IllegalArgumentException::new);
        Assert.isNotEmpty(encode, IllegalArgumentException::new);
        String md5Encoded = Md5Util.md5Hex(plain, StringUtils.EMPTY);
        return encode.equals(md5Encoded);
    }

    @Override
    public boolean matches(String plain, String salt, String encode) {
        Assert.isNotEmpty(plain, IllegalArgumentException::new);
        Assert.isNotEmpty(salt, IllegalArgumentException::new);
        Assert.isNotEmpty(encode, IllegalArgumentException::new);
        String md5Encoded = Md5Util.md5Hex(plain, salt);
        return encode.equals(md5Encoded);
    }
}
