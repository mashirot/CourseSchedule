package ski.mashiro.util;

import org.threeten.extra.Days;
import org.threeten.extra.Seconds;
import org.threeten.extra.Weeks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author MashiroT
 */
public class WeekUtils {

    public static int getCurrWeek(Date termStartDate) {
        LocalDateTime startDate = LocalDateTime.ofInstant(termStartDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        int betweenWeek = Weeks.between(now, startDate).getAmount();
        int betweenDay = Days.between(now, startDate).getAmount();
        int betweenSecond = Seconds.between(now, startDate).getAmount();
        if (betweenDay % 7 == 0 || betweenDay <= 0) {
            if (betweenDay == 0 && betweenSecond > 0) {
                return betweenWeek;
            }
            return betweenWeek + 1;
        }
        return betweenWeek;
    }
}
