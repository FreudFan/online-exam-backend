package edu.sandau.security;

import edu.sandau.config.properties.ApplicationProperties;
import edu.sandau.entity.User;
import edu.sandau.utils.JacksonUtil;
import edu.sandau.utils.RedisConstants;
import edu.sandau.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SessionUtils {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ApplicationProperties properties;

    final public static String USER_ID_PREFIX = "user_Id";
    final public static String USER_wxID_PREFIX = "user_wxId";
    final public static String USER_INFO_PREFIX = "user";
    /*** session 创建时间 */
    final public static String CREATED_TIME = "created_time";
    /*** session 刷新时间 */
    final public static String REFRESH_TIME = "refresh_time";

    public void addUserToSession(User user) {
        Map<String,String> attribute = new HashMap<>(2);
        attribute.put(USER_ID_PREFIX, JacksonUtil.toJSON(user.getId()));
        attribute.put(USER_wxID_PREFIX, JacksonUtil.toJSON(user.getWxId()));
        attribute.put(USER_INFO_PREFIX, JacksonUtil.toJSON(user));
        log.info("用户 {} 已登录 user={}", user.getUsername(), JacksonUtil.toJSON(user));
        setAttribute(attribute);
    }

    public void addTokenToCache(String token) {
        RequestContent.add(token);
    }

    public void addUserToCache(User user) {
        RequestContent.add(user);
    }

    public void addContentToCache(String token, User user) {
        RequestContent.add(user);
        RequestContent.add(token);
    }

    public void setAttribute(Map<String,String> attribute) {
        String token = this.getToken();
        redisTemplate.opsForHash().putAll(token, attribute);
        redisTemplate.expire(token, properties.getTimeout().getRedis(), TimeUnit.MINUTES);
    }

    public void setAttribute(String name, String value) {
        String token = this.getToken();
        redisTemplate.opsForHash().put(token, name, value);
    }

    public void refresh() {
        String token = this.getToken();
        redisTemplate.opsForHash().put(token, SessionUtils.REFRESH_TIME, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        redisTemplate.expire(token, properties.getTimeout().getRedis(), TimeUnit.MINUTES);
    }

    public <T> T getAttribute(String PREFIX, Class<T> clazz) {
        String key = this.getToken();
        String value = String.valueOf(redisTemplate.opsForHash().get(key, PREFIX));
        return JacksonUtil.fromJSON(value, clazz);
    }

    public void removeAttribute(String PREFIX) {
        String key = this.getToken();
        redisTemplate.opsForHash().delete(key, PREFIX);
    }

    public void clean() {
        RequestContent.remove();
    }

    public String createToken() {
        String token = RedisUtil.createKey(RedisConstants.SESSION_ID);
        try {
            redisTemplate.opsForHash().put(this.getToken(token), SessionUtils.CREATED_TIME, String.valueOf(Calendar.getInstance().getTimeInMillis()));
            redisTemplate.expire(this.getToken(token), properties.getTimeout().getRedis(), TimeUnit.MINUTES);
            return token;
        } catch (Exception e) {
            log.error("注册session失败", e);
            redisTemplate.delete(this.getToken(token));
            return null;
        }
    }

    public String getToken() {
        try {
            String token = RequestContent.getSessionToken();
            if(token == null) {
                throw new Exception("Authorization Token不存在");
            } else {
                return RedisConstants.SESSION_ID + ":" + token;
            }
        } catch (Exception e) {
            log.error("Authorization Token不存在", e);
        }
        return null;
    }

    public String getToken(String token) {
        return RedisConstants.SESSION_ID + ":" + token;
    }
}
