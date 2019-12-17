package edu.sandau.utils;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonsUtils {

    public static Object mapToObject(Map map, Class<?> beanClass) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = beanClass.newInstance();
            BeanUtils.populate(obj, map);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static List<Object> mapToObject(List<Map<String, Object>> mapList, Class<?> beanClass) {
        List<Object> list = new ArrayList();
        for ( Map map: mapList ) {
            Object object = mapToObject(map, beanClass);
            list.add(object);
        }
        return list;
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new BeanMap(obj);
    }

}
