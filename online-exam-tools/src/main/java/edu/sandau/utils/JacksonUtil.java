package edu.sandau.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonUtil {

    /***
     * 对象序列化为json字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

    /***
     * 将json字符串序列化为对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJSON(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj;
        try {
            obj = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}
