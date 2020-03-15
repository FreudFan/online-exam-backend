package edu.sandau.security;

import edu.sandau.entity.User;
import edu.sandau.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Auth
@Slf4j
public class RequestFilter implements ContainerRequestFilter {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        boolean access = true;
        try {
            int userId = NumberUtils.toInt(httpSession.getAttribute(SessionUtils.USER_ID_PREFIX).toString());
            User user = userService.getUserById(userId);
            RequestContent.add(user);
            access = false;
        } catch (Exception e) {
            log.warn("用户未登录", e);
        }
        //拦截
        if (access) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("请先登录哦~")
                    .build());
        }
    }
}
