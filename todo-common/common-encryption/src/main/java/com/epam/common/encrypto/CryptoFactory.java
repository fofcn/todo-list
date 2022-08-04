package com.epam.common.encrypto;

import com.epam.common.encrypto.asymmetric.AsymmetricCrypto;
import com.epam.common.encrypto.digest.DigestEncoder;
import com.epam.common.encrypto.symmetric.SymmetricCrypto;

public final class CryptoFactory {

    private CryptoFactory() {}

    public static DigestEncoder createDigestEncoder() {
        throw new UnsupportedOperationException();
    }

    public static SymmetricCrypto createSymmetricCrypto() {
        throw new UnsupportedOperationException();
    }

    public static AsymmetricCrypto createAsymmetricCrypto() {
        throw new UnsupportedOperationException();
    }
}
