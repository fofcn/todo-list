package com.epam.common.encrypto.digest;

import com.epam.common.core.lang.Assert;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class HMacDigestEncoder implements DigestEncoder {

    private final String algorithm;

    public HMacDigestEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String encode(String plain) {
        Assert.isNotNull(Optional.of(plain), () -> new NullPointerException("Plain text can not be null"));

        MessageDigest messageDigest = createDigest(this.algorithm);
        byte[] digest = messageDigest.digest(plain.getBytes(StandardCharsets.UTF_8));
        byte[] hexBytes = new Hex().encode(digest);
        return new String(hexBytes, StandardCharsets.UTF_8);
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

    private static MessageDigest createDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No such hashing algorithm", e);
        }
    }

    private Mac createMac(String algorithm) {
        try {
            return Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No such hashing algorithm: " + algorithm, e);
        }
    }
}
