package edu.sandau.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static StringRedisTemplate redisTemplate;
    public RedisUtil(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }
    /**
     * 缓存生存时间 秒
     */
    private final Integer EXPIRE = 4*60*60;

    /**
     * 生成唯一的key
     * 若已存在相同key，重新生成随机数
     * @return
     */
    public static String createKey() {
        String uuid = UUID.randomUUID().toString();
        while ( redisTemplate.hasKey(uuid) ) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    /***
     * 生成唯一key
     * @param model 模块名
     * @return
     */
    public static String createKey(String model) {
        String uuid = UUID.randomUUID().toString();
        String key = model + ":" + uuid;
        while ( redisTemplate.hasKey(key) ) {
            uuid = UUID.randomUUID().toString();
            key = model + ":" + uuid;
        }
        return uuid;
    }
}
