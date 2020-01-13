package edu.sandau.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 生成唯一的key
     * 若已存在相同key，重新生成随机数
     * @return
     */
    public String createKey() {
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
    public String createKey(String model) {
        String uuid = UUID.randomUUID().toString();
        String key = model + ":" + uuid;
        while ( redisTemplate.hasKey(key) ) {
            uuid = UUID.randomUUID().toString();
            key = model + ":" + uuid;
        }
        return uuid;
    }
}
