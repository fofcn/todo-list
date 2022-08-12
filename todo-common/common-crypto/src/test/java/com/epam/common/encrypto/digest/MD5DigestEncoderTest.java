package com.epam.common.encrypto.digest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MD5DigestEncoderTest {

    private MD5DigestEncoder md5DigestEncoder = new MD5DigestEncoder();

    @Test
    void test_normal_encode_without_salt() {
        String plain = RandomStringUtils.randomAlphabetic(64);
        String digester = md5DigestEncoder.encode(plain);
        assertNotNull(digester);
    }

    @Test
    void test_normal_encode_with_salt() {
        String plain = RandomStringUtils.randomAlphabetic(64);
        String salt = RandomStringUtils.randomAlphabetic(32);
        String digester = md5DigestEncoder.encode(plain, salt);
        assertNotNull(digester);
    }

    @Test
    void test_normal_encode_and_matches() {
        String plain = RandomStringUtils.randomAlphabetic(64);
        String salt = RandomStringUtils.randomAlphabetic(32);

        String digester = md5DigestEncoder.encode(plain);
        boolean matches = md5DigestEncoder.matches(plain, digester);
        assertTrue(matches);

        digester = md5DigestEncoder.encode(plain, salt);
        matches = md5DigestEncoder.matches(plain, salt, digester);
        assertTrue(matches);
    }

    @Test
    void testEncodeWith_InputNullOrEmptyParameters_thenReturnException() {
        String randStr = RandomStringUtils.randomAlphabetic(64);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.encode(null));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.encode(null, randStr));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.encode(randStr, null));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.encode(null, null));
        assertNotNull(illegalArgumentException);
    }

    @Test
    void testMatchesWith_InputNullOrEmptyParameters_thenReturnException() {
        String randStr = RandomStringUtils.randomAlphabetic(64);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.matches(null, randStr));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.matches(randStr, null));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.matches(null, null));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> md5DigestEncoder.matches(null, null, null));
        assertNotNull(illegalArgumentException);
    }
}
