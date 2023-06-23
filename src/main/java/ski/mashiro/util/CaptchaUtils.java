package ski.mashiro.util;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

/**
 * @author MashiroT
 */
public class CaptchaUtils {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 45;
    public static final int LENGTH = 4;

    public static String[] generate() {
        var captcha = new SpecCaptcha(WIDTH, HEIGHT, LENGTH);
        return new String[]{captcha.text().toLowerCase(), captcha.toBase64()};
    }
}
