package ski.mashiro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.threeten.extra.Days;
import org.threeten.extra.Weeks;

import java.time.LocalDateTime;
import java.util.Map;

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

    @Test
    void testJwt() {
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.e30.P9s-RJyHFLfoSJvo_UXZF0lRaa-btVWHO1McsRixEcEsiDBl1NOonYAhxVYgzDmvvi0FkFfIaHTLB_X0j3MaoA".split(" ")[1];
//        Assertions.assertTrue(JwtUtils.verifyToken(token, "111111"));
    }

    @Test
    void testRedisHashGet(@Autowired StringRedisTemplate stringRedisTemplate) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("");
        System.out.println(entries.isEmpty());
        System.out.println(entries);
    }
}
