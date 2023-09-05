package ski.mashiro.dto;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class ApiCourseSearchDTO {
    private String username;
    private String apiToken;
    private String dayOfWeek;
    private Boolean isEffective;
}
