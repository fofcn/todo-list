package com.epam.common.encrypto.digest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HMacDigestEncoderTest {

    private HMacDigestEncoder encoder;

    @Test
    void testEncodeWithoutSalt() {
        String str = RandomStringUtils.random(64);
        encoder = new HMacDigestEncoder(DigestAlgorithm.ALG_SHA256.getValue());
        String md5Encode = encoder.encode(str);
        assertNotNull(md5Encode);
    }

    @Test
    void testEncodeWithSalt() {
        String str = RandomStringUtils.random(64);
        String salt = RandomStringUtils.random(16);
        encoder = new HMacDigestEncoder(DigestAlgorithm.ALG_SHA256.getValue());
        String md5EncodeSalt = encoder.encode(str, salt);
        assertNotNull(md5EncodeSalt);
    }

    @Test
    void testMatchWithoutSalt() {
        String str = RandomStringUtils.random(64);
        encoder = new HMacDigestEncoder(DigestAlgorithm.ALG_SHA256.getValue());
        String md5Encode = encoder.encode(str);
        boolean matches = encoder.matches(str, md5Encode);
        assertTrue(matches);
    }

    @Test
    void testMatchWithSalt() {
        String str = RandomStringUtils.random(64);
        String salt = RandomStringUtils.random(16);
        encoder = new HMacDigestEncoder(DigestAlgorithm.ALG_SHA256.getValue());
        String md5EncodeSalt = encoder.encode(str, salt);
        boolean matches = encoder.matches(str, salt, md5EncodeSalt);
        assertTrue(matches);
    }

    @Test
    void testNoSuchAlgorithmException() {
        String str = RandomStringUtils.random(64);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            DigestEncoder noSuchEncoder = new HMacDigestEncoder("UNKNOWN_HMAC_ALGORITHM");
            noSuchEncoder.encode(str);
        });

        assertNotNull(illegalArgumentException);
    }
}
