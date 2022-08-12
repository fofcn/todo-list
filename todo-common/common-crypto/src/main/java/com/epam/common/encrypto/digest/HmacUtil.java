package com.epam.common.encrypto.digest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtil {

    private HmacUtil() {}

    public static final String ALG_MD5 = "HmacMD5";

    public static final String ALG_SHA1 = "HmacSHA1";

    public static final String ALG_SHA256 = "HmacSHA256";

    public static final String ALG_SHA384= "HmacSHA384";

    public static final String ALG_SHA512 = "HmacSHA512";

    public static final String ALG_SM3 = "HmacSM3";

    public static final String ALG_SM4 = "SM4CMAC";

    public static String hmacMD5(String plain, String key) {
        return hmac(ALG_MD5, plain, key);
    }

    public static String hmacSHA1(String plain, String key) {
        return hmac(ALG_SHA1, plain, key);
    }

    public static String hmacSHA256(String plain, String key) {
        return hmac(ALG_SHA256, plain, key);
    }

    public static String hmacSHA384(String plain, String key) {
        return hmac(ALG_SHA384, plain, key);
    }

    public static String hmacSHA512(String plain, String key) {
        return hmac(ALG_SHA512, plain, key);
    }


    public static String hmac(String algorithm, String plain, String key) {
        if (StringUtils.isBlank(algorithm) || StringUtils.isBlank(plain) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("algorithm or plain text or key is blank.");
        }

        if (ALG_MD5.equals(algorithm) || ALG_SHA1.equals(algorithm)) {
            throw new SecurityException("Cryptographic algorithm HmacMD5 and HmacSHA1 is weak and should not be used.");
        }

        // Get an hmac_sha1 key from the raw key bytes
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, algorithm);

        try {
            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }

    }
}
