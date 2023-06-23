package ski.mashiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

/**
 * @author MashiroT
 */
public class JwtUtils {

    private static final String ALGORITHM_SALT = "a";

    public static String createToken(String username) {
        return JWT.create()
                .sign(Algorithm.HMAC512(username + ALGORITHM_SALT));
    }

    public static boolean verifyToken(String authToken, String username) {
        try {
            JWT.require(Algorithm.HMAC512(username + ALGORITHM_SALT))
                    .build()
                    .verify(authToken);
            return true;
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return false;
        }
    }
}
