package ski.mashiro.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.dto.Result;
import ski.mashiro.util.CaptchaUtils;
import ski.mashiro.vo.CaptchaVo;

import javax.servlet.http.HttpServletRequest;

import static ski.mashiro.constant.StatusCodeConstants.CAPTCHA_GENERATE_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@CrossOrigin
@RequestMapping("/utils")
public class UtilController {

    @GetMapping("/captcha")
    public Result<CaptchaVo> generateCaptcha(HttpServletRequest request){
        String[] captcha = CaptchaUtils.generate();
        request.getSession().setAttribute("captcha", captcha[0]);
        return Result.success(CAPTCHA_GENERATE_SUCCESS, new CaptchaVo(captcha[1]));
    }
}
