package edu.sandau.security;

import edu.sandau.entity.User;
import edu.sandau.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class SessionUtils {
    @Autowired
    private HttpSession httpSession;

    final public static String USER_ID_PREFIX = "user_Id";
    final public static String USER_wxID_PREFIX = "user_wxId";

    public void addUserToSession(User user) {
        httpSession.setAttribute(USER_ID_PREFIX, user.getWxId());
        RequestContent.add(user);
        log.info("用户 {} 已登录 user={}", user.getUsername(), JacksonUtil.toJSON(user));
    }

}
