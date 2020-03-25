package edu.sandau.security;

import edu.sandau.entity.User;

import javax.ws.rs.container.ContainerRequestContext;

/***
 * 请求内容
 */
public class RequestContent {
    /** 用户 */
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void add(User user) {
        userHolder.set(user);
    }

    public static void add(String token) {
        tokenHolder.set(token);
    }

    public static User getCurrentUser() {
        return userHolder.get();
    }

    public static String getSessionToken() {
        return tokenHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        tokenHolder.remove();
    }
}
