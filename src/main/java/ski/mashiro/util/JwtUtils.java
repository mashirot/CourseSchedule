package ski.mashiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

/**
 * @author MashiroT
 */
public class JwtUtils {

    private static final int EXPIRE_TIME = 15 * 60 * 1000;
    private static final String ALGORITHM_SALT = "a";

    public static String createToken(int uid) {
        return JWT.create()
                .withClaim("uid", uid)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC512(uid + ALGORITHM_SALT));
    }

    public static boolean verifyToken(String authToken, String uid) {
        try {
            JWT.require(Algorithm.HMAC512(uid + ALGORITHM_SALT)).build().verify(authToken);
            return true;
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return false;
        }
    }
}
