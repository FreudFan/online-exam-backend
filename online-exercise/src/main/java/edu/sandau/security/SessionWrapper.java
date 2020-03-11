package edu.sandau.security;

import edu.sandau.config.VariableConfig;
import edu.sandau.entity.User;
import edu.sandau.utils.JacksonUtil;
import edu.sandau.utils.RedisConstants;
import edu.sandau.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 管理redis-session的工具类
 * session 管理 redis 的 key
 * @author Fan
 */
@Slf4j
@Component
public class SessionWrapper{

    private final VariableConfig variableConfig;
    private final StringRedisTemplate redisTemplate;
    private final HttpSession httpSession;
    /***
     * redis-session的生存时间
     */
    private static Integer SESSION_TIMEOUT;

    public SessionWrapper(VariableConfig variableConfig, StringRedisTemplate redisTemplate, HttpSession httpSession) {
        this.variableConfig = variableConfig;
        SESSION_TIMEOUT = variableConfig.session_timeout;
        this.redisTemplate = redisTemplate;
        this.httpSession = httpSession;
    }

    /***
     * !!! 此方法只允许在用户登录时使用，并只允许使用一次 !!!
     * 使用场景：用户登录成功后需在redis里集中管理session，在此处生成唯一的 key
     * 唯一key生成策略：model + 随机数
     * 会将用户对象信息存入redis，key会在下一次发送请求时保存在 session 里
     * redis 保存成功后会将 key 返回给前端，前端需在每次请求头中加上 "Authorization" : key
     * @return key 会话唯一标识
     * @param user 用户实体
     */
    public String addSessionToRedis(User user) {
        //确保redis的key唯一
        String uuid = RedisUtil.createKey(RedisConstants.SESSION_ID);
        String key = this.getId(uuid);
        Map<String,String> attribute = new HashMap<>(1);
        attribute.put("user", JacksonUtil.toJSON(user));
        redisTemplate.opsForHash().putAll(key, attribute);
        //设置超时时间10秒 第三个参数控制
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
        addContentToCache(key, user, null);
        return uuid;
    }

    /***
     * 将用户信息添加进session和线程池
     * @param key 用户session key
     * @param user 用户实体
     * @param requestContext 可以为 null
     */
    public void addContentToCache(String key, User user, ContainerRequestContext requestContext) {
        //当前信息添加进session
        httpSession.setAttribute("key", key);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("user", user);
        //添加进 RequestContent
        RequestContent.add(user);
        RequestContent.add(key);
        RequestContent.add(requestContext);
        log.info("用户 {} 已登录：key={} user={}", user.getUsername(), key, JacksonUtil.toJSON(user));
    }

    /***
     * 从session里拿redis的key
     * @return
     */
    public String getId() {
        String token = RequestContent.getSessionKey();
        return RedisConstants.SESSION_ID + ":" + token;
    }
    public String getId(String token) {
        return RedisConstants.SESSION_ID + ":" + token;
    }

    /***
     * 获取当前用户
     * @return
     */
    public User getCurrentUser() {
        String key = "";
        String value = "";
        try {
            key = this.getId();
            value =  Objects.requireNonNull(redisTemplate.opsForHash().get(key, "user")).toString();
            return JacksonUtil.fromJSON(value, User.class);
        } catch (Exception e) {
            log.error("获取用户信息失败: key={} value={}", key, value);
            log.error("Exception:", e);
            return null;
        }
    }

    /***
     * 根据 session key 获取用户信息
     * @param key
     * @return
     */
    public User getUser(String key) {
        String value = "";
        try {
            key = this.getId(key);
            value = Objects.requireNonNull(redisTemplate.opsForHash().get(key, "user")).toString();
            return JacksonUtil.fromJSON(value, User.class);
        } catch (Exception e) {
            log.error("获取用户信息失败: key={} value={}", key, value);
            log.error("Exception:", e);
            return null;
        }
    }

    /***
     * 添加属性
     * @param name
     * @param value
     */
    public void setAttribute(String name, Object value) {
        String key = this.getId();
        redisTemplate.opsForHash().put(key,name,value);
    }

    /***
     * 获取所有属性名
     * @return
     */
    public Set getAttributeNames() throws Exception{
        String key = this.getId();
        return redisTemplate.opsForHash().keys(key);
    }

    /***
     * 设置 session 寿命
     * @param interval 单位：分钟
     */
    public void setMaxInactiveInterval(int interval) throws Exception{
        SESSION_TIMEOUT = interval;
        this.refresh();
    }

    /***
     * 获得redis session的最大存活时间
     * @return
     */
    public int getMaxInactiveInterval() throws Exception{
        return SESSION_TIMEOUT;
    }

    /***
     * 获取指定属性 用过name查找
     * @param name
     * @return
     */
    public Object getAttribute(String name) throws Exception{
        String key = this.getId();
        return redisTemplate.opsForHash().get(key, name);
    }

    /***
     * 删除指定属性
     * @param name
     */
    public void removeAttribute(String name) {
        String key = this.getId();
        redisTemplate.opsForHash().delete(key, name);
    }

    /***
     * 获取当前用户所有属性
     * @return
     */
    public Map<Object, Object> getAllAttribute() throws Exception{
        String key = this.getId();
        return redisTemplate.opsForHash().entries(key);
    }

    /***
     * 刷新redis key过期时间
     */
    public void refresh() {
        String key = this.getId();
        //设置超时时间10秒 第三个参数控制
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }
    public void refresh(String key) {
        key = this.getId(key);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    /***
     * 使session失效，从redis里删除
     */
    public void invalidate() {
        String key = this.getId();
        redisTemplate.delete(key);
    }

}
