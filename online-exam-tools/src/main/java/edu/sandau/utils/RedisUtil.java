package edu.sandau.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 生成唯一的key,长度为32位
     * @return
     */
    public String createKey() {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        while ( redisTemplate.hasKey(uuid) ) {   //若已存在相同key，重新生成随机数
            uuid = UUID.randomUUID().toString().replaceAll("-","");
        }
        return uuid;
    }
}
