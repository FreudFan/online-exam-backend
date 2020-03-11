package edu.sandau.security;


import edu.sandau.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Auth
@Slf4j
public class RequestFilter implements ContainerRequestFilter {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //获取客户端Header中提交的token
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        //判断用户是否已登录
        boolean access = false;
        if (StringUtils.isNotEmpty(token)) {
            try {
                User user = sessionWrapper.getUser(token);
                //用户存在
                if (user != null && user.getId() > -1) {
                    sessionWrapper.addContentToCache(token, user, requestContext);
                    sessionWrapper.refresh(token);
                    access = true;
                }
            } catch (Exception e) {
                log.error("解析用户失败 key={}", token);
                log.error("Exception:", e);
            }
        }
        //拦截
        if ( !access ) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("请先登录哦~")
                    .build());
        }
    }
}
