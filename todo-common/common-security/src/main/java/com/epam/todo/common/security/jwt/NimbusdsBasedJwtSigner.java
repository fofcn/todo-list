package com.epam.todo.common.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyTypeException;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.crypto.impl.ECDSAProvider;
import com.nimbusds.jose.crypto.impl.MACProvider;
import com.nimbusds.jose.crypto.impl.RSASSAProvider;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jose.util.Base64URL;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;

public class NimbusdsBasedJwtSigner implements JwtSigner {

    @Override
    public String sign(String base64Header, String base64Payload, Key key) {
       JWSSigner jwsSigner = createJWSSigner(base64Header, key);
        try {
            JWSHeader jwsHeader = JWSHeader.parse(Base64URL.from(base64Header));
            byte[] composeSignBytes = composeSignBytes(base64Header, base64Payload);
            Base64URL base64Sign = jwsSigner.sign(jwsHeader, composeSignBytes);
            return base64Sign.toString();
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e);
        }

    }

    @Override
    public boolean verify(String base64Header, String base64Payload, String base64Sign, Key key) {
        try {
            JWSHeader jwsHeader = JWSHeader.parse(Base64URL.from(base64Header));
            String signerBody = base64Header + '.' + base64Payload;
            JWSVerifierFactory verifierFactory = new DefaultJWSVerifierFactory();
            JWSVerifier jwsVerifier = verifierFactory.createJWSVerifier(jwsHeader, key);
            return jwsVerifier.verify(jwsHeader, signerBody.getBytes(StandardCharsets.UTF_8),
                    Base64URL.from(base64Sign));
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e);
        }
    }

    private JWSSigner createJWSSigner(String base64Header, Key key) {
        JWSSigner jwsSigner;
        try {
            JWSHeader jwsHeader = JWSHeader.parse(Base64URL.from(base64Header));
            if (MACProvider.SUPPORTED_ALGORITHMS.contains(jwsHeader.getAlgorithm())) {
                if (!(key instanceof SecretKey)) {
                    throw new JwtException(new KeyTypeException(SecretKey.class));
                }
                SecretKey macKey = (SecretKey) key;
                jwsSigner = new MACSigner(macKey);

            } else if (RSASSAProvider.SUPPORTED_ALGORITHMS.contains(jwsHeader.getAlgorithm())) {

                if (!(key instanceof RSAPrivateKey)) {
                    throw new JwtException(new KeyTypeException(SecretKey.class));
                }

                RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
                jwsSigner = new RSASSASigner(rsaPrivateKey);

            } else if (ECDSAProvider.SUPPORTED_ALGORITHMS.contains(jwsHeader.getAlgorithm())) {

                if (!(key instanceof ECPrivateKey)) {
                    throw new KeyTypeException(ECPrivateKey.class);
                }

                ECPrivateKey ecPrivateKey = (ECPrivateKey) key;
                jwsSigner = new ECDSASigner(ecPrivateKey);

            } else {
                throw new JwtException("Unsupported JWS algorithm: " + jwsHeader.getAlgorithm());
            }
        } catch (ParseException | JOSEException e) {
           throw new JwtException(e);
        }

        return jwsSigner;
    }

    private byte[] composeSignBytes(String base64Header, String base64Body) {
        String signString = base64Header + '.' + base64Body;
        return signString.getBytes(StandardCharsets.UTF_8);
    }

}
