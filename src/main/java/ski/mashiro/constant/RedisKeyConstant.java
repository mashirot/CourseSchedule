package ski.mashiro.constant;

/**
 * @author MashiroT
 */
public class RedisKeyConstant {
    public static final String SCHEDULE_KEY = "schedule:";
    public static final String USER_KEY = SCHEDULE_KEY + "user:";
    public static final String COURSE_KEY = SCHEDULE_KEY + "course:";
    public static final String USER_USERNAME = ":username";
    public static final String USER_INFO = ":info";
    public static final String USER_CURR_WEEK = ":currWeek";
    public static final String USER_CAPTCHA = ":captcha";
    public static final String COURSE_ALL = ":all";
}
