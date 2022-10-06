package ski.mashiro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MashiroT
 */
public class Utils {

    public static String transitionTableName(String userCode) {
        return "tb_" + userCode;
    }

    public static String transitionDateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date transitionStrToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    public static String calcCurrentWeek(Date currentDate, Date initDate) {
        return String.valueOf((currentDate.getTime() - initDate.getTime()) / (7 * 24 * 60 * 60 * 1000) + 1);
    }
}
