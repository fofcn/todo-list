package com.epam.common.encrypto.asymmetric;

import com.epam.common.core.lang.Assert;
import com.epam.common.encrypto.KeyUtil;
import com.epam.common.encrypto.exception.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AbstractAsymmetricCrypto implements AsymmetricCrypto {

    private final String algorithm;

    private final PublicKey publicKey;

    private final PrivateKey privatekey;

    private final Cipher cipher;

    public AbstractAsymmetricCrypto(String algorithm) {
        this(algorithm, (byte[])null, (byte[])null);
    }

    public AbstractAsymmetricCrypto(String algorithm, String publicKey, String privateKey) {
        this(algorithm, KeyUtil.decodeHex(publicKey), KeyUtil.decodeHex(privateKey));
    }

    public AbstractAsymmetricCrypto(String algorithm, byte[] publicKey, byte[] privateKey) {
        this(algorithm,  KeyUtil.generatePublicKey(algorithm, publicKey), KeyUtil.generatePrivateKey(algorithm, privateKey));
    }

    public AbstractAsymmetricCrypto(String algorithm, PublicKey publicKey, PrivateKey privateKey) {
        this.algorithm = algorithm;
        this.publicKey = publicKey;
        this.privatekey = privateKey;
        try {
            this.cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] bytes, KeyType keyType) {
        Assert.isNotNull(bytes, () -> new IllegalArgumentException("bytes"));
        Assert.isNotNull(keyType, () -> new IllegalArgumentException("keyType"));
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, privatekey);
            return this.cipher.doFinal(bytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] bytes, KeyType keyType) {
        Assert.isNotNull(bytes, () -> new IllegalArgumentException("bytes"));
        Assert.isNotNull(keyType, () -> new IllegalArgumentException("keyType"));

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return this.cipher.doFinal(bytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptoException(e);
        }
    }
}
