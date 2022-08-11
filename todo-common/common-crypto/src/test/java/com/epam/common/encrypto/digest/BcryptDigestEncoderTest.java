package com.epam.common.encrypto.digest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BcryptDigestEncoderTest {

    private BcryptDigestEncoder bcryptDigestEncoder = new BcryptDigestEncoder();

    @Test
    void testNormalEncode() {
        String str = RandomStringUtils.random(64);
        String hashed = bcryptDigestEncoder.encode(str);
        assertNotNull(hashed);

        String sameStrHashed = bcryptDigestEncoder.encode(str);
        assertNotEquals(sameStrHashed, hashed);

        String salt = BCrypt.gensalt();
        String saltedHashed = bcryptDigestEncoder.encode(str, salt);
        assertNotNull(saltedHashed);
        String sameSaltedPlainHashed = bcryptDigestEncoder.encode(str, BCrypt.gensalt());
        assertNotEquals(saltedHashed, sameSaltedPlainHashed);
    }

    @Test
    void testEncode_with_Null_or_Empty_plain() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> bcryptDigestEncoder.encode(null));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> bcryptDigestEncoder.encode(StringUtils.EMPTY));
        assertNotNull(illegalArgumentException);

        String str = RandomStringUtils.random(64);
        String salt = BCrypt.gensalt();
        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> bcryptDigestEncoder.encode(StringUtils.EMPTY, salt));
        assertNotNull(illegalArgumentException);

        illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> bcryptDigestEncoder.encode(str, StringUtils.EMPTY));
        assertNotNull(illegalArgumentException);
    }

    @Test
    void testMatchesNormal() {
        String str = RandomStringUtils.random(64);
        String salt = BCrypt.gensalt();

        String hashed = bcryptDigestEncoder.encode(str);
        boolean matches = bcryptDigestEncoder.matches(str, hashed);
        assertTrue(matches);

        String saltHashed = bcryptDigestEncoder.encode(str, salt);
        matches = bcryptDigestEncoder.matches(str, salt, saltHashed);
        assertTrue(matches);
    }

    @Test
    void testMismatch() {
        String str = RandomStringUtils.random(64);
        String salt = BCrypt.gensalt();
        String paddingStr = RandomStringUtils.random(16);

        String hashed = bcryptDigestEncoder.encode(str);
        boolean matches = bcryptDigestEncoder.matches(paddingStr + str , hashed);
        assertFalse(matches);

        String saltHashed = bcryptDigestEncoder.encode(str, salt);
        matches = bcryptDigestEncoder.matches(paddingStr + str , salt, saltHashed);
        assertFalse(matches);
    }

}
