package ski.mashiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author MashiroT
 */
public class JwtUtils {

    private static final String ALGORITHM_SALT = "MashiroT";
    private static final int EXPIRATION = 24 * 60 * 60 * 1000;

    public static String createToken(int uid) {
        return JWT.create()
                .withClaim("uid", uid)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .withIssuer("mashirot")
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(ALGORITHM_SALT));
    }

    public static DecodedJWT getVerifier(String authToken) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(ALGORITHM_SALT))
                .withIssuer("mashirot")
                .build()
                .verify(authToken);
    }
}
