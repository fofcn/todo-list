package com.epam.common.encrypto.digest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class Md5Util {

    public static String md5Hex(String plain, String salt) {
        String toBeEncode = plain;
        if (StringUtils.isNotEmpty(salt)) {
            toBeEncode += salt;
        }

        return DigestUtils.md5Hex(toBeEncode);
    }

    public static String md5Hex16(String plain, String salt) {
        String toBeEncode = plain;
        if (StringUtils.isNotEmpty(salt)) {
            toBeEncode += salt;
        }

        String md5Hex = md5Hex(toBeEncode, salt);
        return md5Hex.substring(8, 24);
    }
}
