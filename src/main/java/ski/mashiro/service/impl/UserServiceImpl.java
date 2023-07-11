package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ski.mashiro.dao.UserDao;
import ski.mashiro.dto.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.JwtUtils;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.vo.UserInfoVo;
import ski.mashiro.vo.UserLoginVo;
import ski.mashiro.vo.UserRegVo;

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
    public Result<String> saveUser(UserRegVo userReg) {
        var user = new User(userReg.getUsername(), userReg.getPassword(), userReg.getTermStartDate(), userReg.getTermEndDate());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setApiToken(DigestUtils.md5DigestAsHex(BCrypt.hashpw(user.getUsername() + user.getTermStartDate() + user.getTermEndDate(), BCrypt.gensalt()).getBytes()));
        if (userDao.saveUser(user) == 1) {
            return Result.success(USER_REG_SUCCESS, null);
        }
        return Result.failed(USER_REG_FAILED, null);
    }

    @Override
    public Result<UserLoginVo> getUserByPassword(UserLoginVo userLogin) {
        var user = new User(userLogin.getUsername(), userLogin.getPassword());
        var currUser = userDao.getUserByUsername(user.getUsername());
        boolean checkPw = BCrypt.checkpw(user.getPassword(), currUser.getPassword());
        if (checkPw) {
            String authToken = JwtUtils.createToken(user.getUsername());
            String key = USER_KEY + currUser.getUsername();
            try {
                stringRedisTemplate.opsForValue().set(key + USER_UID, currUser.getUid().toString(), 24, TimeUnit.HOURS);
                UserInfoVo userInfoVo = new UserInfoVo(currUser.getUsername(), currUser.getTermStartDate(), currUser.getTermEndDate(), null, currUser.getApiToken());
                stringRedisTemplate.opsForValue().set(key + USER_INFO, objectMapper.writeValueAsString(userInfoVo), 24, TimeUnit.HOURS);
                return Result.success(USER_LOGIN_SUCCESS, new UserLoginVo(authToken));
            } catch (Exception e) {
                log.warn(e.getMessage());
                return Result.failed(SYSTEM_ERR, null);
            }
        }
        return Result.failed(USER_LOGIN_FAILED, null);
    }

    @Override
    public Result<UserInfoVo> getUserInfoByUsername(String username) {
        String key = USER_KEY + username;
        try {
            String infoCache;
            if ((infoCache = stringRedisTemplate.opsForValue().get(key + USER_INFO)) != null) {
                UserInfoVo userInfo = objectMapper.readValue(infoCache, UserInfoVo.class);
                Integer currWeek;
                String currWeekStr;
                if ((currWeekStr = stringRedisTemplate.opsForValue().get(key + USER_CURR_WEEK)) != null) {
                    currWeek = Integer.parseInt(currWeekStr);
                } else {
                    currWeek = WeekUtils.getCurrWeek(userInfo.getTermStartDate());
                    stringRedisTemplate.opsForValue().set(key + USER_CURR_WEEK, String.valueOf(currWeek), 30, TimeUnit.MINUTES);
                }
                userInfo.setCurrWeek(currWeek);
                return new Result<>(USER_INFO_SUCCESS, userInfo, null);
            }
        } catch (JsonProcessingException e) {
            log.warn("用户 {} CacheInfo 序列化失败：{}", username, e.getMessage());
        }
        var user = userDao.getUserByUsername(username);
        int currWeek = WeekUtils.getCurrWeek(user.getTermStartDate());
        try {
            stringRedisTemplate.opsForValue().set(key + USER_UID, user.getUid().toString(), 24, TimeUnit.HOURS);
            UserInfoVo userInfoVo = new UserInfoVo(user.getUsername(), user.getTermStartDate(), user.getTermEndDate(), null, user.getApiToken());
            stringRedisTemplate.opsForValue().set(key + USER_INFO, objectMapper.writeValueAsString(userInfoVo), 24, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(key + USER_CURR_WEEK, String.valueOf(currWeek), 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            return Result.failed(SYSTEM_ERR, null);
        }
        return Result.success(USER_INFO_SUCCESS, new UserInfoVo(user.getUsername(), user.getTermStartDate(), user.getTermEndDate(), currWeek, user.getApiToken()));
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
    public Result<User> getApiTokenByUsername(User user) {
        String token = userDao.getApiTokenByUsername(user.getUsername());
        if (token == null) {
            return Result.failed(USER_GET_API_FAILED, null);
        }
        user.setApiToken(token);
        return Result.success(USER_GET_API_SUCCESS, user);
    }

    @Override
    public Result<String> updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        if (userDao.updateUser(user) > 0) {
            stringRedisTemplate.delete(USER_KEY + user.getUsername() + USER_INFO);
            return Result.success(USER_MODIFY_SUCCESS, null);
        }
        return Result.failed(USER_MODIFY_FAILED, null);
    }
}
