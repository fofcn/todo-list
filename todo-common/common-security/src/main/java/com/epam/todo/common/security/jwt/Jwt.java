package com.epam.todo.common.security.jwt;

import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Jwt {

    private final JwtHeader header;

    private final JwtPayload payload;

    private final long validSeconds;

    private final JwtSigner jwtSigner = new NimbusdsBasedJwtSigner();

    private final List<String> tokenParts = new ArrayList<>(3);

    public static Jwt of() {
        return new Jwt();
    }

    public static Jwt of(String token) {
        return of(token, 7200L);
    }

    public static Jwt of(String token, long validSeconds) {
        return new Jwt(token, validSeconds);
    }

    public Jwt() {
        this(null, 7200);
    }

    public Jwt(String token, long validSeconds) {
        this.header = new JwtHeader();
        this.payload = new JwtPayload();
        this.validSeconds = validSeconds;
        parse(token);
    }

    public boolean verify(Key key) {
        // verify token structure
        // verify sign
        // verify date
        boolean isSignCorrect = verifySign(key);
        boolean isExpired = verifyExpireDate();
        return isSignCorrect && isExpired;
    }

    public boolean verifySign(Key key) {
        return jwtSigner.verify(tokenParts.get(0), tokenParts.get(1), tokenParts.get(2), key);
    }

    public boolean verifyExpireDate() {
        LocalDateTime expireDate = this.payload.getExpireDate();
        long diff = LocalDateTime.now().until(expireDate, ChronoUnit.SECONDS);
        return diff > validSeconds;
    }

    private void parse(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        splitToken(token);
        this.header.parse(this.tokenParts.get(0));
        this.payload.parse(this.tokenParts.get(1));
    }

    private void splitToken(String token) {
        List<String> parseParts = Stream.of(token.split("\\.")).collect(Collectors.toList());
        if (parseParts.size() < 3) {
            throw new JwtException("token expect contains three parts but got " + parseParts.size() + " parts.");
        }
        this.tokenParts.addAll(parseParts);
    }
}
