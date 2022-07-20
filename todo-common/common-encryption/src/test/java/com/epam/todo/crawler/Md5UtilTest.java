package com.epam.todo.crawler;

import com.epam.todo.crawler.digest.Md5Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Md5UtilTest {

    @Test
    public void testNormalMd5Hex() {
        String plain = "abc";
        String salt = "abc";
        String encode1 = Md5Util.md5Hex(plain, salt);
        assertNotNull(encode1);

        String encode2 = Md5Util.md5Hex(plain, null);
        assertNotNull(encode2);

        String encode3 = Md5Util.md5Hex16(plain, salt);
        assertEquals(encode1.substring(8, 24), encode3);

        String encode4 = Md5Util.md5Hex16(plain, null);
        assertEquals(encode2.substring(8, 24), encode4);
    }
}
