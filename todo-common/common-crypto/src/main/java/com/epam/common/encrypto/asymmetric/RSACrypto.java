package com.epam.common.encrypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RSACrypto extends AbstractAsymmetricCrypto {

    public RSACrypto(String algorithm, PublicKey publicKey, PrivateKey privateKey) {
        super(algorithm, publicKey, privateKey);
    }
}
