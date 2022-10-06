package ski.mashiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ski.mashiro.util.Utils;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestSomething {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSerialize() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("Sunday", "10:00-11:30");
        map.put("Monday", "14:00-15:30");
        String rs = objectMapper.writeValueAsString(map);
        System.out.println(rs);
    }

    @Test
    void testDate() {
        System.out.println(Utils.transitionTableName("123"));
        System.out.println(Utils.transitionTableName("321"));
    }
}
