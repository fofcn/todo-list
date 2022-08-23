package com.epam.todo.common.security.jwt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Jwt {

    private final JwtHeader header;

    private final JwtPayload payload;

    private final List<String> tokenParts = new ArrayList<>(3);

    public static Jwt of(String token) {
        return new Jwt(token);
    }

    public Jwt(String token) {
        this.header = new JwtHeader();
        this.payload = new JwtPayload();
        parse(token);
    }

    public boolean isValidate() {
        // validate signer
        // validate expire date
        // validate nbt date
        // validate other
        return false;
    }

    private void parse(String token) {
        splitToken(token);
        this.header.parse(this.tokenParts.get(0));
        this.payload.parse(this.tokenParts.get(1));
    }

    private void splitToken(String token) {
        List<String> tokenParts = Stream.of(token.split(".")).collect(Collectors.toList());
        if (tokenParts.size() < 3) {
            throw new JwtException("token expect contains three parts but got " + tokenParts.size() + " parts.");
        }
        this.tokenParts.addAll(tokenParts);
    }
}
