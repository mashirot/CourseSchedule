package ski.mashiro.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

/**
 * @author MashiroT
 */
public class Encrypt {
    public static String generateSalt(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        char c;
        for (int i = 0; i < length; i++) {
            c = (char) (random.nextInt(94) + 33);
            sb.append(c);
        }
        return sb.toString();
    }
    public static String encryptPassword(String password, String salt) {
        String addSaltPasswd = password.substring(0, password.length() / 2) + salt + password.substring(password.length() / 2);
        return DigestUtils.sha3_256Hex(addSaltPasswd).substring(20,52);
    }
}
