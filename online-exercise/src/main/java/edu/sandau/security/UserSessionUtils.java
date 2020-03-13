package edu.sandau.security;

import edu.sandau.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class UserSessionUtils {
    final static String USER_ID_PREFIX = "userId";
    @Autowired
    private HttpSession httpSession;

    public void addUserToSession(User user) {
        httpSession.setAttribute(USER_ID_PREFIX, user.getId());
        RequestContent.add(user);
    }

}
