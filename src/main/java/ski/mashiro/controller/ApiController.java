package ski.mashiro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.dto.ApiCourseSearchDTO;
import ski.mashiro.dto.CourseDTO;
import ski.mashiro.service.ApiService;
import ski.mashiro.common.Result;

import java.util.List;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/sel")
    public Result<List<CourseDTO>> listCourseByCondition(@RequestBody ApiCourseSearchDTO apiCourseSearchDTO) {
        return apiService.verifyAndSel(apiCourseSearchDTO);
    }
}
