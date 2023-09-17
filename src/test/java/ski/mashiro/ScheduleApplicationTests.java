package ski.mashiro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import ski.mashiro.util.WeekUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class ScheduleApplicationTests {

    @Test
    void testCurrWeek() {
        LocalDateTime date = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        System.out.println(date);
        Date from = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(from);
        System.out.println(WeekUtils.getCurrWeek(from));
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
