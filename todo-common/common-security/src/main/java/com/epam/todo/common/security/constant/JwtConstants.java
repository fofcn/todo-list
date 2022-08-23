package com.epam.todo.common.security.constant;

public interface JwtConstants {

    /**
     * jwt signer
     */
    String ISSUER = "iss";

    /**
     * jwt subject
     */
    String SUBJECT = "sub";

    /**
     * audience
     */
    String AUDIENCE = "aud";

    /**
     * jwt expire date
     */
    String EXPIRES_AT = "exp";

    /**
     * time of take effect which used for the time of jwt effective time.
     */
    String NOT_BEFORE = "nbf";

    /**
     * jwt sign time
     */
    String ISSUED_AT = "iat";

    /**
     * jwt identity
     */
    String JWT_ID = "jti";

    /**
     * sign algorithm
     */
    String ALGORITHM = "alg";

    /**
     * jwt type, commonly is JWT
     */
    String JWT_TYPE = "typ";

    /**
     * content type
     */
    String CONTENT_TYPE = "cty";

    /**
     * jwk identity
     */
    String KEY_ID = "kid";

}
