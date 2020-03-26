package edu.sandau.security;

import edu.sandau.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Auth
@Slf4j
public class RequestFilter implements ContainerRequestFilter {
    @Autowired
    private SessionUtils sessionUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        boolean access = true;
        try {
            if (StringUtils.isNotEmpty(token) && redisTemplate.hasKey(sessionUtils.getToken(token))) {
                RequestContent.add(token);
                User user = sessionUtils.getAttribute(SessionUtils.USER_INFO_PREFIX, User.class);
                //用户存在
                if (user != null && user.getId() > -1) {
                    RequestContent.add(user);
                    sessionUtils.addUserToSession(user);
                }
                access = false;
                sessionUtils.refresh();
            }
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
