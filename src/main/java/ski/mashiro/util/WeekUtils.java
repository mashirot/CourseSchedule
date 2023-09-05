package ski.mashiro.util;

import org.threeten.extra.Days;
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
        int betweenWeek = Weeks.between(startDate, now).getAmount();
        int betweenDay = Days.between(startDate, now).getAmount();
        if (betweenDay % 7 == 0 || betweenDay >= 0) {
            return betweenWeek + 1;
        }
        return betweenWeek;
    }
}
