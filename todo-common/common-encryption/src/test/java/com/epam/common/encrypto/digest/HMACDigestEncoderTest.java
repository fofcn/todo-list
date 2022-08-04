package com.epam.common.encrypto.digest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HMACDigestEncoderTest {

    private HMACDigestEncoder encoder;

    @Test
    void testEncodeWithoutSalt() {
        String str = RandomStringUtils.random(64);
        encoder = new HMACDigestEncoder(DigestAlgorithm.ALG_MD5.getValue());
        String md5Encode = encoder.encode(str);
        assertNotNull(md5Encode);
    }

    @Test
    void testEncodeWithSalt() {
        String str = RandomStringUtils.random(64);
        String salt = RandomStringUtils.random(16);
        String md5EncodeSalt = encoder.encode(str, salt);
        assertNotNull(md5EncodeSalt);
    }

    @Test
    void testMatchWithoutSalt() {
        String str = RandomStringUtils.random(64);
        String md5Encode = encoder.encode(str);

        boolean matches = encoder.matches(str, md5Encode);
        assertTrue(matches);
    }

    @Test
    void testMatchWithSalt() {
        String str = RandomStringUtils.random(64);
        String salt = RandomStringUtils.random(16);
        String md5EncodeSalt = encoder.encode(str, salt);

        boolean matches = encoder.matches(str, salt, md5EncodeSalt);
        assertTrue(matches);
    }
}
