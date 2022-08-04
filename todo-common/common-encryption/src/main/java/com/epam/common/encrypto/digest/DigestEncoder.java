package com.epam.common.encrypto.digest;

public interface DigestEncoder {

    String encode(String plain);

    String encode(String plain, String salt);

    boolean matches(String plain, String encode);

    boolean matches(String plain, String salt, String encode);
}
