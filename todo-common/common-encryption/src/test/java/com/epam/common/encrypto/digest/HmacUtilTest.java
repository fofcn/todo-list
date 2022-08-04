package com.epam.common.encrypto.digest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HmacUtilTest {

    private String plain = "abc";
    private String key = "abc";

    @Test
    void testHmac() {
        String digest = HmacUtil.hmac(HmacUtil.ALG_SHA256, plain, key);
        System.out.println(digest);
        assertNotNull(digest);

        digest = HmacUtil.hmac(HmacUtil.ALG_SHA384, plain, key);
        System.out.println(digest);
        assertNotNull(digest);

        digest = HmacUtil.hmac(HmacUtil.ALG_SHA512, plain, key);
        System.out.println(digest);
        assertNotNull(digest);

        digest = HmacUtil.hmac(HmacUtil.ALG_SHA512, plain, key);
        System.out.println(digest);
        assertNotNull(digest);
    }

    @Test
    void testHmacMD5() {
        String digest = HmacUtil.hmacMD5(plain, key);
        assertNotNull(digest);
    }

    @Test
    void testHmacSHA1() {
        String digest = HmacUtil.hmacSHA1(plain, key);
        assertNotNull(digest);
    }

    @Test
    void testHmacSHA256() {
        String digest = HmacUtil.hmacSHA256(plain, key);
        assertNotNull(digest);
    }

    @Test
    void testHmacSHA384() {
        String digest = HmacUtil.hmacSHA384(plain, key);
        assertNotNull(digest);
    }

    @Test
    void testHmacSHA512() {
        String digest = HmacUtil.hmacSHA512(plain, key);
        assertNotNull(digest);
    }

    @Test
    void testHmacNull() {
        assertNull(HmacUtil.hmac(HmacUtil.ALG_SHA512, null, null));
    }

    @Test
    void testWeakMacAlgMD5AndSHA1() {
        String plain = RandomStringUtils.random(64);
        String key = RandomStringUtils.random(32);
        SecurityException securityException = assertThrows(SecurityException.class, () -> {
            HmacUtil.hmacSHA1(plain, key);
        });
        assertNotNull(securityException);
        assertEquals("Cryptographic algorithm HmacMD5 and HmacSHA1 is weak and should not be used.", securityException.getMessage());

        securityException = assertThrows(SecurityException.class, () -> {
            HmacUtil.hmacMD5(plain, key);
        });
        assertNotNull(securityException);
        assertEquals("Cryptographic algorithm HmacMD5 and HmacSHA1 is weak and should not be used.", securityException.getMessage());
    }
}
