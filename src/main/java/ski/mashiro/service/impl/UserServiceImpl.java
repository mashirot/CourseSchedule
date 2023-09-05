package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ski.mashiro.dao.UserDao;
import ski.mashiro.common.Result;
import ski.mashiro.entity.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.JwtUtils;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.dto.UserInfoDTO;
import ski.mashiro.dto.UserLoginDTO;
import ski.mashiro.dto.UserRegDTO;

import java.util.concurrent.TimeUnit;

import static ski.mashiro.constant.RedisKeyConstant.*;
import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public UserServiceImpl(UserDao userDao, ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Result<String> saveUser(UserRegDTO userReg) {
        var user = new User(userReg.getUsername(), userReg.getPassword(), userReg.getTermStartDate(), userReg.getTermEndDate());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setApiToken(DigestUtils.md5DigestAsHex(BCrypt.hashpw(user.getUsername() + user.getTermStartDate() + user.getTermEndDate(), BCrypt.gensalt()).getBytes()));
        if (userDao.saveUser(user) == 1) {
            return Result.success(USER_REG_SUCCESS, null);
        }
        return Result.failed(USER_REG_FAILED, null);
    }

    @Override
    public Result<UserLoginDTO> getUserByPassword(UserLoginDTO userLogin) {
        var user = new User(userLogin.getUsername(), userLogin.getPassword());
        var currUser = userDao.getUserByUsername(user.getUsername());
        boolean checkPw = BCrypt.checkpw(user.getPassword(), currUser.getPassword());
        if (checkPw) {
            String authToken = JwtUtils.createToken(currUser.getUid());
            String key = USER_KEY + currUser.getUid();
            try {
                stringRedisTemplate.opsForValue().set(key + USER_USERNAME, currUser.getUsername(), 24, TimeUnit.HOURS);
                UserInfoDTO userInfoDTO = new UserInfoDTO(currUser.getUsername(), currUser.getTermStartDate(), currUser.getTermEndDate(), null, currUser.getApiToken());
                stringRedisTemplate.opsForValue().set(key + USER_INFO, objectMapper.writeValueAsString(userInfoDTO), 24, TimeUnit.HOURS);
                return Result.success(USER_LOGIN_SUCCESS, new UserLoginDTO(authToken));
            } catch (Exception e) {
                log.warn(e.getMessage());
                return Result.failed(SYSTEM_ERR, null);
            }
        }
        return Result.failed(USER_LOGIN_FAILED, null);
    }

    @Override
    public Result<UserInfoDTO> getUserInfoByUsername(int uid) {
        String key = USER_KEY + uid;
        try {
            String infoCache;
            if ((infoCache = stringRedisTemplate.opsForValue().get(key + USER_INFO)) != null) {
                UserInfoDTO userInfo = objectMapper.readValue(infoCache, UserInfoDTO.class);
                Integer currWeek;
                String currWeekStr;
                if ((currWeekStr = stringRedisTemplate.opsForValue().get(key + USER_CURR_WEEK)) != null) {
                    currWeek = Integer.parseInt(currWeekStr);
                } else {
                    currWeek = WeekUtils.getCurrWeek(userInfo.getTermStartDate());
                    stringRedisTemplate.opsForValue().set(key + USER_CURR_WEEK, String.valueOf(currWeek), 30, TimeUnit.MINUTES);
                }
                userInfo.setCurrWeek(currWeek);
                return Result.success(USER_INFO_SUCCESS, userInfo);
            }
        } catch (JsonProcessingException e) {
            log.warn("用户 {} CacheInfo 序列化失败：{}", uid, e.getMessage());
        }
        return Result.failed(USER_INFO_FAILED, null);
    }

    @Override
    public Result<User> getUserByApiToken(User user) {
        User rsUser = userDao.getUserByApiToken(user.getUsername(), user.getApiToken());
        if (rsUser != null) {
            return Result.success(USER_LOGIN_SUCCESS, rsUser);
        }
        return Result.failed(USER_LOGIN_FAILED, null);
    }

    @Override
    public Result<String> updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        if (userDao.updateUser(user) > 0) {
            String key = USER_KEY + user.getUid();
            String username = stringRedisTemplate.opsForValue().get(key + USER_USERNAME);
            var newUser = userDao.getUserByUsername(username);
            try {
                stringRedisTemplate.opsForValue().set(key + USER_USERNAME, newUser.getUsername(), 24, TimeUnit.HOURS);
                UserInfoDTO userInfoDTO = new UserInfoDTO(newUser.getUsername(), newUser.getTermStartDate(), newUser.getTermEndDate(), null, newUser.getApiToken());
                stringRedisTemplate.opsForValue().set(key + USER_INFO, objectMapper.writeValueAsString(userInfoDTO), 24, TimeUnit.HOURS);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            return Result.success(USER_MODIFY_SUCCESS, null);
        }
        return Result.failed(USER_MODIFY_FAILED, null);
    }
}
