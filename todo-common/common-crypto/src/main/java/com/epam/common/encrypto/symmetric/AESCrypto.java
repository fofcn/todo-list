package com.epam.common.encrypto.symmetric;

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

public class AESCrypto extends AbstractSymmetricCrypto {

    public AESCrypto(SymmetricAlgorithm algorithm) {
        super(algorithm);
    }

    public AESCrypto(SymmetricAlgorithm algorithm, byte[] key) {
        super(algorithm, key);
    }

    public AESCrypto(SymmetricAlgorithm algorithm, SecretKey secretKey) {
        super(algorithm, secretKey);
    }

    public AESCrypto(SymmetricAlgorithm algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
        super(algorithm, key, paramsSpec);
    }
}
