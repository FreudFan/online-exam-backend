package com.sandau.onlineexam.controller;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    /**
     * request是一个字符串，其中有很多参数key构成的字符串，把key这个字符串提出来，转化成对应类型
     * @param request
     * @param key
     * @return
     */
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {
            String keyStr = request.getParameter((key));
            if (keyStr != null) {
                keyStr = keyStr.trim(); // 如果keyStr不为空，那么就去掉两侧的空格
            }
            if ("".equals(keyStr)) {
                keyStr = null;  // 如果keyStr为空，那么keyStr为null
            }
            return keyStr;
        } catch (Exception e) {
            return null;
        }
    }

}
