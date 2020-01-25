package edu.sandau.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.sandau.rest.model.User;
import edu.sandau.utils.RedisConstants;
import edu.sandau.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.SecurityContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 管理redis-session的工具类
 * 支持使用 SecurityContext 或 session 管理redis的key
 * 如果使用 SecurityContext 管理 token，请在 controller 层调用，在其它层会出现注入 SecurityContext 为空情况
 * @author Fan
 */
@Component
public class SessionWrapper{
    /***
     * redis-session的生存时间
     */
    private static Integer SESSION_TIMEOUT;

    @Value(value="${redis.session_timeout:30}")
    public void setSessionTimeout(Integer sessionTimeout) {
        SESSION_TIMEOUT = sessionTimeout;
    }

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtil redisUtil;
    private final HttpSession httpSession;
    public SessionWrapper(RedisTemplate<String, Object> redisTemplate, RedisUtil redisUtil, HttpSession httpSession) {
        this.redisTemplate = redisTemplate;
        this.redisUtil = redisUtil;
        this.httpSession = httpSession;
    }

    /***
     * !!! 此方法只允许在用户登录时使用，并只允许使用一次 !!!
     * 使用场景：用户登录成功后需在redis里集中管理session，在此处生成唯一的 key
     * 唯一key生成策略：model + 随机数
     * 会将用户对象信息存入redis，key会在下一次发送请求时保存在 SecurityContext 和 session 里
     * redis 保存成功后会将 key 返回给前端，前端需在每次请求头中加上 "Authorization" : key
     * @return key 会话唯一标识
     * @param user 用户实体
     */
    public String addSessionToRedis(User user) {
        //确保redis的key唯一
        String uuid = redisUtil.createKey(RedisConstants.SESSION_ID);
        String key = this.getId(uuid);
        Map<String,String> attribute = new HashMap<>(1);
        attribute.put("user", JSON.toJSON(user).toString());
        redisTemplate.opsForHash().putAll(key, attribute);
        //设置超时时间10秒 第三个参数控制
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
        //当前信息添加进session
        httpSession.setAttribute("key", key);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("user", user);
        return uuid;
    }

    /***
     * 从session里拿redis的key
     * @return
     */
    public String getId() {
        String token = httpSession.getAttribute("key").toString();
        return RedisConstants.SESSION_ID + ":" + token;
    }
    public String getId(String token) {
        return RedisConstants.SESSION_ID + ":" + token;
    }
    public String getId(SecurityContext securityContext) {
        String token = securityContext.getAuthenticationScheme();
        return RedisConstants.SESSION_ID + ":" + token;
    }

    /***
     * 获取当前用户
     * @return
     */
    public User getCurrentUser() {
        try {
            String key = this.getId();
            String value =  Objects.requireNonNull(redisTemplate.opsForHash().get(key, "user")).toString();
            return JSONObject.parseObject(value, User.class);
        } catch (Exception e) {
            return null;
        }
    }
    public User getCurrentUser(SecurityContext securityContext) {
        try {
            String key = this.getId(securityContext);
            String value = Objects.requireNonNull(redisTemplate.opsForHash().get(key, "user")).toString();
            return JSONObject.parseObject(value, User.class);
        } catch (Exception e) {
            return null;
        }
    }
    public User getCurrentUser(String key) {
        String value = "";
        try {
            key = this.getId(key);
            value = Objects.requireNonNull(redisTemplate.opsForHash().get(key, "user")).toString();
            return JSONObject.parseObject(value, User.class);
        } catch (Exception e) {
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
    public void setAttribute(String name, Object value, SecurityContext securityContext) {
        String key = securityContext.getAuthenticationScheme();
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
    public Set getAttributeNames(SecurityContext securityContext) throws Exception{
        String key = securityContext.getAuthenticationScheme();
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
    public void setMaxInactiveInterval(int interval, SecurityContext securityContext) throws Exception{
        SESSION_TIMEOUT = interval;
        this.refresh(this.getId(securityContext));
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
    public Object getAttribute(String name, SecurityContext securityContext) throws Exception{
        String key = securityContext.getAuthenticationScheme();
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
    public void removeAttribute(String name, SecurityContext securityContext) {
        String key = securityContext.getAuthenticationScheme();
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
    public Map<Object, Object> getAllAttribute(SecurityContext securityContext) throws Exception{
        String key = securityContext.getAuthenticationScheme();
        if ( key == null ) {
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /***
     * 获取当前用户 id
     * @return
     */
    public Integer getUserId() {
        String id = httpSession.getAttribute("userId").toString();
        return Integer.parseInt(id);
    }
    public Integer getUserId(SecurityContext securityContext) {
        return Integer.parseInt(securityContext.getUserPrincipal().getName());
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
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }
    public void refresh(SecurityContext securityContext) {
        String key = this.getId(securityContext);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    /***
     * 使session失效，从redis里删除
     */
    public void invalidate() {
        String key = this.getId();
        redisTemplate.delete(key);
    }
    public void invalidate(SecurityContext securityContext) {
        String key = this.getId(securityContext);
        redisTemplate.delete(key);
    }

}
