package ski.mashiro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.threeten.extra.Days;
import org.threeten.extra.Weeks;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
class ScheduleApplicationTests {

    @Test
    void testCurrWeek() {
        LocalDateTime date = LocalDateTime.of(2023, 2, 13, 0, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        int betweenWeek = Weeks.between(date, now).getAmount();
        int betweenDay = Days.between(date, now).getAmount();
        System.out.println(betweenDay % 7 == 0 ? betweenWeek : betweenWeek + 1);
    }
}
