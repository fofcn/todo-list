package com.epam.common.encrypto.asymmetric;

import com.epam.common.encrypto.KeyUtil;
import com.epam.common.encrypto.exception.CryptoException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RSACryptoTest {

    private final String plainText = RandomStringUtils.randomAlphabetic(64);

    private final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);

    private final KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue(), 2048, null, null);

    private final RSACrypto rsaCrypto = new RSACrypto(AsymmetricAlgorithm.RSA.getValue(), keyPair.getPublic(), keyPair.getPrivate());

    @Test
    void testEncryptWithPublicKey() {
        byte[] encrypted = rsaCrypto.encrypt(plainText.getBytes(StandardCharsets.UTF_8), KeyType.PUBLIC_KEY);
        assertNotNull(encrypted);
        assertTrue(encrypted.length > 0);
    }

    @Test
    void testEncryptWithPrivateKey() {
        byte[] encrypted = rsaCrypto.encrypt(plainText.getBytes(StandardCharsets.UTF_8), KeyType.PRIVATE_KEY);
        assertNotNull(encrypted);
        assertTrue(encrypted.length > 0);
    }

    @Test
    void testEncryptWhenBytesOrKeyTypeIsNullThenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> rsaCrypto.encrypt(null, KeyType.PUBLIC_KEY));
        assertThrows(IllegalArgumentException.class, () -> rsaCrypto.encrypt(plainText.getBytes(StandardCharsets.UTF_8), null));
    }

    @Test
    void testConstructorWhenInvalidKeyThenThrowsException() {
        assertThrows(CryptoException.class, () -> new RSACrypto(AsymmetricAlgorithm.RSA.getValue(), "1".getBytes(), "2".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testDecryptWithPublicKey() {
        byte[] encryptedWithPrivateKey = rsaCrypto.encrypt(plainBytes, KeyType.PRIVATE_KEY);
        assertNotNull(encryptedWithPrivateKey);

        byte[] decryptedWithPublicKey = rsaCrypto.decrypt(encryptedWithPrivateKey, KeyType.PUBLIC_KEY);
        assertNotNull(decryptedWithPublicKey);

        assertArrayEquals(plainBytes, decryptedWithPublicKey);
    }

    @Test
    void testDecryptWithPrivateKey() {
        byte[] encryptedWithPublicKey = rsaCrypto.encrypt(plainBytes, KeyType.PUBLIC_KEY);
        assertNotNull(encryptedWithPublicKey);

        byte[] decryptedWithPrivateKey = rsaCrypto.decrypt(encryptedWithPublicKey, KeyType.PRIVATE_KEY);
        assertNotNull(decryptedWithPrivateKey);

        assertArrayEquals(plainBytes, decryptedWithPrivateKey);
    }

    @Test
    void testDecryptWhenByteOrKeyTypeIsNullThenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> rsaCrypto.decrypt(null, KeyType.PUBLIC_KEY));
        assertThrows(IllegalArgumentException.class, () -> rsaCrypto.decrypt(plainText.getBytes(StandardCharsets.UTF_8), null));
    }
}
