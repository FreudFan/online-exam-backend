package edu.sandau.security;

import edu.sandau.entity.User;

import javax.ws.rs.container.ContainerRequestContext;

/***
 * 请求内容
 */
public class RequestContent {
    /** 用户 */
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();
    /** 用户 session key */
    private static final ThreadLocal<String> keyHolder = new ThreadLocal<>();
    /** 当前请求 */
    private static final ThreadLocal<ContainerRequestContext> requestHolder = new ThreadLocal<>();

    public static void add(User user) {
        userHolder.set(user);
    }

    public static void add(String key) {
        keyHolder.set(key);
    }

    public static void add(ContainerRequestContext request) {
        requestHolder.set(request);
    }

    public static ContainerRequestContext getCurrentRequest() {
        return requestHolder.get();
    }

    public static User getCurrentUser() {
        return userHolder.get();
    }

    public static String getSessionKey() {
        return keyHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
        keyHolder.remove();
    }
}
