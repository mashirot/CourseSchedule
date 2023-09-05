package ski.mashiro.util;

import ski.mashiro.entity.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author MashiroT
 */
public class FileUtils {

    public static Course courseStringDeserialize(String str, int uid) {
//        name|place|teacher|dayOfWeek|startTime-endTime|startWeek-endWeek|oddWeek|credit
        String[] items = str.split("\\|");
        Course course = new Course();
        course.setUid(uid);
        course.setName(items[0]);
        course.setPlace(items[1]);
        course.setTeacher(items[2]);
        course.setDayOfWeek(items[3]);
        String[] times = items[4].split("-");
        course.setStartTime(times[0]);
        course.setEndTime(times[1]);
        String[] weeks = items[5].split("-");
        course.setStartWeek(Integer.parseInt(weeks[0]));
        course.setEndWeek(Integer.parseInt(weeks[1]));
        course.setOddWeek("否".equals(items[6]) ? 0 : "单".equals(items[6]) ? 1 : 2);
        course.setCredit(Double.parseDouble(items[7]));
        return course;
    }

    public static Queue<String> fileDeserialize(BufferedReader reader) throws IOException {
        Queue<String> strQueue = new LinkedList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            strQueue.offer(line.trim());
        }
        return strQueue;
    }

}
