package edu.sandau.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.sandau.entity.LoginUser;
import edu.sandau.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.SecurityContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 * 管理redis-session的工具类
 * 支持使用 SecurityContext 或 session 管理redis的key
 * 如果使用 SecurityContext 管理 token，请在 controller 层调用，在其它层会出现注入 SecurityContext 为空情况
 */
@Component
public class SessionWrapper{
    @Value("${redis.session_timeout:30}")
    private Integer SESSION_TIMEOUT;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private HttpSession httpSession;

    /***
     * !!! 此方法只允许在用户登录时使用，并只允许使用一次 !!!
     * 使用场景：用户登录成功后需在redis里集中管理session，在此处生成唯一的 key
     * 唯一key生成策略：auth + 随机数
     * 会将用户对象信息存入redis，key会在下一次发送请求时保存在 SecurityContext 里
     * redis 保存成功后会将 key 发给前端，前端需在每次请求头中加上 "Authorization" : key
     * @return key 会话唯一标识
     * @param loginUser
     */
    public String addSessionToRedis(LoginUser loginUser) {
        //确保redis的key唯一
        String key = redisUtil.createKey();
        Map attribute = new HashMap(1);
        attribute.put("user", JSON.toJSON(loginUser).toString());
        redisTemplate.opsForHash().putAll(key, attribute);
        //设置超时时间10秒 第三个参数控制
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
        //当前信息添加进session
        httpSession.setAttribute("key", key);
        httpSession.setAttribute("userId", loginUser.getLogin_user_id());
        httpSession.setAttribute("user", loginUser);
        return key;
    }

    /***
     * 获取当前用户
     * @return
     */
    public LoginUser getCurrentUser() {
        String key = this.getId();
        String value =  redisTemplate.opsForHash().get(key, "user").toString();
        LoginUser user = JSONObject.parseObject(value, LoginUser.class);
        return user;
    }
    public LoginUser getCurrentUser(SecurityContext securityContext) {
        String key = securityContext.getAuthenticationScheme();
        String value =  redisTemplate.opsForHash().get(key, "user").toString();
        LoginUser user = JSONObject.parseObject(value, LoginUser.class);
        return user;
    }
    public LoginUser getCurrentUser(String key) {
        String value =  redisTemplate.opsForHash().get(key, "user").toString();
        LoginUser user = JSONObject.parseObject(value, LoginUser.class);
        return user;
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
    public Set getAttributeNames() {
        String key = this.getId();
        return redisTemplate.opsForHash().keys(key);
    }
    public Set getAttributeNames(SecurityContext securityContext) {
        String key = securityContext.getAuthenticationScheme();
        return redisTemplate.opsForHash().keys(key);
    }

    /***
     * 设置 session 寿命
     * @param interval 单位：分钟
     */
    public void setMaxInactiveInterval(int interval) {
        this.SESSION_TIMEOUT = interval;
        this.refresh();
    }
    public void setMaxInactiveInterval(int interval, SecurityContext securityContext) {
        this.SESSION_TIMEOUT = interval;
        this.refresh(this.getId(securityContext));
    }

    /***
     * 获得redis session的最大存活时间
     * @return
     */
    public int getMaxInactiveInterval() {
        return this.SESSION_TIMEOUT;
    }

    /***
     * 获取指定属性 用过name查找
     * @param name
     * @return
     */
    public Object getAttribute(String name) {
        String key = this.getId();
        return redisTemplate.opsForHash().get(key, name);
    }
    public Object getAttribute(String name, SecurityContext securityContext) {
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
    public Map<String, Object> getAllAttribute() {
        String key = this.getId();
        return (Map<String ,Object>) redisTemplate.opsForHash().entries(key);
    }
    public Map<String, Object> getAllAttribute(SecurityContext securityContext) {
        String key = securityContext.getAuthenticationScheme();
        if ( key == null ) {
            return null;
        }
        return (Map<String ,Object>) redisTemplate.opsForHash().entries(key);
    }

    /***
     * 从session里拿redis的key
     * @return
     */
    public String getId() {
        return httpSession.getAttribute("key").toString();
    }
    public String getId(SecurityContext securityContext) {
        return securityContext.getAuthenticationScheme();
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
        String key = getId(securityContext);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    /***
     * 使session失效，从redis里删除
     */
    public void invalidate() {
        String key = httpSession.getAttribute("key").toString();
        redisTemplate.delete(key);
    }
    public void invalidate(SecurityContext securityContext) {
        String key = getId(securityContext);
        redisTemplate.delete(key);
    }

}
