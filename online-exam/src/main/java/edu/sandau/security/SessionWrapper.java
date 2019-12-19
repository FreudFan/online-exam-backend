package edu.sandau.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.sandau.model.LoginUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class SessionWrapper {
    @Value("${session.session_timeout:30}")
    private Integer SESSION_TIMEOUT;
    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * !!! 此方法只允许在用户登录时使用，并只允许使用一次 !!!
     * 使用场景：用户登录成功后需在redis里集中管理session，在此处生成唯一的 key
     * 唯一key规则：sessionId + 随机数
     * 会将用户对象信息存入redis，key保存在服务器的session里
     * redis 保存成功后会将 key 发给前端，前端需在每次请求中加上 key
     * @return key 会话唯一标识
     * @param httpSession
     */
    public String addSessionToRedis(HttpSession httpSession, LoginUsers loginUsers) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String key = "sessionId" + uuid; //确保redis的key唯一
        while ( redisTemplate.hasKey(key) ) {   //若已存在相同key，重新生成随机数
            uuid = UUID.randomUUID().toString().replaceAll("-","");
            key = "sessionId" + uuid;
        }
        Map attribute = new HashMap();
        attribute.put("user", JSON.toJSON(loginUsers).toString());
        httpSession.setAttribute("key", key); //把redis的key加进tomcat的session
        redisTemplate.opsForHash().putAll(key, attribute);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES); //设置超时时间10秒 第三个参数控制
        return key;
    }

    /***
     * 获取当前用户
     * @param httpSession
     * @return
     */
    public LoginUsers getCurrentUser(HttpSession httpSession) {
        String key = getId(httpSession);
        if ( key == null ) return null;
        String value =  redisTemplate.opsForHash().get(key, "user").toString();
        LoginUsers user = JSONObject.parseObject(value, LoginUsers.class);
        return user;
    }

    /***
     * 添加属性
     * @param httpSession
     * @param name
     * @param value
     */
    public void setAttribute(HttpSession httpSession, String name, Object value) {
        String key = getId(httpSession);
        redisTemplate.opsForHash().put(key,name,value);
    }

    /***
     * 获取所有属性名
     * @param httpSession
     * @return
     */
    public Set getAttributeNames(HttpSession httpSession) {
        String key = getId(httpSession);
        return redisTemplate.opsForHash().keys(key);
    }

    /***
     * 设置 session 寿命
     * @param httpSession
     * @param interval 单位：分钟
     */
    public void setMaxInactiveInterval(HttpSession httpSession, int interval) {
        this.SESSION_TIMEOUT = interval;
        refresh(httpSession);
    }

    /***
     * 获取指定属性 用过name查找
     * @param httpSession
     * @param name
     * @return
     */
    public Object getAttribute(HttpSession httpSession, String name) {
        String key = getId(httpSession);
        return redisTemplate.opsForHash().get(key, name);
    }

    /***
     * 删除指定属性
     * @param httpSession
     * @param name
     */
    public void removeAttribute(HttpSession httpSession, String name) {
        String key = getId(httpSession);
        redisTemplate.opsForHash().delete(key, name);
    }

    /***
     * 获取当前用户所有属性
     * @param httpSession
     * @return
     */
    public Map<String, Object> getAllAttribute(HttpSession httpSession) {
        String key = getId(httpSession);
        if ( key == null ) return null;
        return (Map<String ,Object>) redisTemplate.opsForHash().entries(key);
    }

    /***
     * 从session里拿redis的key
     * @param httpSession
     * @return
     */
    public String getId(HttpSession httpSession) {
         Object attr = httpSession.getAttribute("key");
         if ( attr != null ) {
             return attr.toString();
         }
         return null;
    }

    /***
     * 刷新redis key过期时间
     * @param httpSession
     */
    public void refresh(HttpSession httpSession) {
        String key = getId(httpSession);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES); //设置超时时间10秒 第三个参数控制
    }

    /***
     * 使session失效，从redis里删除
     * @param httpSession
     */
    public void invalidate(HttpSession httpSession) {
        String key = getId(httpSession);
        redisTemplate.delete(key);
        httpSession.invalidate();
    }

}
