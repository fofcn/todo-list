package com.epam.common.encrypto.digest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACDigestEncoder implements DigestEncoder {

    public final String algorithm;

    public HMACDigestEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String encode(String plain) {
        return null;
    }

    @Override
    public String encode(String plain, String salt) {
        return null;
    }

    @Override
    public boolean matches(String plain, String encode) {
        return false;
    }

    @Override
    public boolean matches(String plain, String salt, String encode) {
        return false;
    }

    public String hmac(String algorithm, String plain, String key) {
        if (StringUtils.isBlank(algorithm) || StringUtils.isBlank(plain) || StringUtils.isBlank(key)) {
            return null;
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, algorithm);

        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            byte[] hexBytes = new Hex().encode(rawHmac);

            return new String(hexBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException ignore) {
        }

        return null;
    }
}
