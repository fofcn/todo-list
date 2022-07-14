package com.epam.todo.encrypto.digest;

import com.epam.todo.crypto.digest.HmacUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HmacUtilTest {

    private String plain = "abc";
    private String key = "abc";

    @Test
    public void testHmac() {
        String digest = HmacUtil.hmac(HmacUtil.ALG_MD5, plain, key);
        System.out.println(digest);
        assertNotNull(digest);

        digest = HmacUtil.hmac(HmacUtil.ALG_SHA256, plain, key);
        System.out.println(digest);
        assertNotNull(digest);

        digest = HmacUtil.hmac(HmacUtil.ALG_SHA1, plain, key);
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
    public void testHmacMD5() {
        String digest = HmacUtil.hmacMD5(plain, key);
        assertNotNull(digest);
    }

    @Test
    public void testHmacSHA1() {
        String digest = HmacUtil.hmacSHA1(plain, key);
        assertNotNull(digest);
    }

    @Test
    public void testHmacSHA256() {
        String digest = HmacUtil.hmacSHA256(plain, key);
        assertNotNull(digest);
    }

    @Test
    public void testHmacSHA384() {
        String digest = HmacUtil.hmacSHA384(plain, key);
        assertNotNull(digest);
    }

    @Test
    public void testHmacSHA512() {
        String digest = HmacUtil.hmacSHA512(plain, key);
        assertNotNull(digest);
    }

    @Test
    public void testHmacNull() {
        assertNull(HmacUtil.hmac(HmacUtil.ALG_SHA512, null, null));
    }


}
