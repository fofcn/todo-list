package com.epam.common.encrypto.digest;

import com.epam.common.core.lang.Assert;
import com.epam.common.encrypto.KeyUtil;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Optional;

public class HMacDigestEncoder implements DigestEncoder {

    private final String algorithm;

    private final SecretKey key;

    private final AlgorithmParameterSpec spec;

    public HMacDigestEncoder(final String algorithm) {
        this(algorithm, null);
    }

    public HMacDigestEncoder(final String algorithm, final SecretKey key) {
        this(algorithm, key, null);
    }

    public HMacDigestEncoder(final String algorithm, final SecretKey key, final AlgorithmParameterSpec spec) {
        this.algorithm = algorithm;
        if (key == null) {
            this.key = KeyUtil.generateKey(this.algorithm);
        } else {
            this.key = key;
        }
        this.spec = spec;
    }

    @Override
    public String encode(String plain) {
        Assert.isNotNull(Optional.of(plain), () -> new NullPointerException("Plain text can not be null"));

        Mac mac = createMac(this.algorithm);

        try {
            if (this.spec == null) {
                mac.init(this.key);
            } else {
                mac.init(this.key, this.spec);
            }

            byte[] rawHMac = mac.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            byte[] hexBytes = new Hex().encode(rawHMac);
            return new String(hexBytes, StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("invalid key", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("invalid algorithm parameter", e);
        }
    }

    @Override
    public String encode(String plain, String salt) {
        String text = plain + salt;
        return encode(text);
    }

    @Override
    public boolean matches(String plain, String encode) {
        Assert.isNotEmpty(plain, () -> new NullPointerException("Plain text can not be null"));
        Assert.isNotEmpty(encode, () -> new NullPointerException("Encoded text can not be null"));
        String newEncode = encode(plain);
        return newEncode.equals(encode);
    }

    @Override
    public boolean matches(String plain, String salt, String encode) {
        Assert.isNotEmpty(plain, () -> new NullPointerException("Plain text can not be null"));
        Assert.isNotEmpty(salt, () -> new NullPointerException("Salt text can not be null"));
        Assert.isNotEmpty(encode, () -> new NullPointerException("Encoded text can not be null"));
        String newEncode = encode(plain, salt);
        return newEncode.equals(encode);
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
