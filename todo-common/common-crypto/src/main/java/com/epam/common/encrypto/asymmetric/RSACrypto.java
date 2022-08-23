package com.epam.common.encrypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RSACrypto extends AbstractAsymmetricCrypto {

    public RSACrypto(String algorithm, PublicKey publicKey, PrivateKey privateKey) {
        super(algorithm, publicKey, privateKey);
    }

    public RSACrypto(String algorithm) {
        super(algorithm);
    }

    public RSACrypto(String algorithm, String publicKey, String privateKey) {
        super(algorithm, publicKey, privateKey);
    }

    public RSACrypto(String algorithm, byte[] publicKey, byte[] privateKey) {
        super(algorithm, publicKey, privateKey);
    }
}
