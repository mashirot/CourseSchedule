package ski.mashiro.util;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.pojo.Course;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String getWeek() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        switch (dayOfWeek) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
            default: return null;
        }
    }

    public static String calcCurrentWeek(Date currentDate, Date initDate) {
        Calendar init = Calendar.getInstance();
        init.setTime(initDate);
        return String.valueOf((currentDate.getTime() - initDate.getTime() - (init.get(Calendar.DAY_OF_WEEK) - 1) * (24 * 60 * 60 * 1000)) / (7 * 24 * 60 * 60 * 1000) + 1);
    }

    public static List<Course> analyzeScheduleFile(MultipartFile multipartFile) {
        try {
            String data = FileCopyUtils.copyToString(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
            String[] courses = data.trim().split("#");
            List<Course> courseList = new ArrayList<>(courses.length);
            for (String datum : courses) {
                if ("".equals(datum)) {
                    continue;
                }
                String strCourse = datum.trim();
                String[] items = strCourse.split("\\|");
                Course course = new Course();
                course.setCourseName(items[0]);
                course.setCourseLocation(items[1]);
                course.setCourseLecturer(items[2]);
                String[] dates = items[3].split(",");
                StringBuilder sb = new StringBuilder();
                for (String date : dates) {
                    sb.append(date).append(date.equals(dates[dates.length - 1]) ? "" : ",");
                }
                course.setCourseDate(sb.toString());
                course.setCourseWeek(items[4].split(","));
                courseList.add(course);
            }
            return courseList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
