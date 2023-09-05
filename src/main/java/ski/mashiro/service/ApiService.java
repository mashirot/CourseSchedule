package ski.mashiro.service;

import ski.mashiro.common.Result;
import ski.mashiro.dto.ApiCourseSearchDTO;
import ski.mashiro.dto.CourseDTO;

import java.util.List;

/**
 * @author MashiroT
 */
public interface ApiService {
    Result<List<CourseDTO>> verifyAndSel(ApiCourseSearchDTO apiCourseSearchDTO);
}
