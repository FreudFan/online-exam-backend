package edu.sandau.security;

import com.alibaba.fastjson.JSONObject;
import edu.sandau.model.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

@Auth
public class RequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //获取客户端Header中提交的token
        String token = requestContext.getHeaderString("Authorization");
        //判断用户是否已登录
        boolean access = true;
        if ( !StringUtils.isEmpty(token) && token.startsWith("auth") ) {
            try {
//                String value = redisTemplate.opsForHash().get(token, "user").toString();
//                JSONObject params = JSONObject.parseObject(value);
//                String userId = params.get("login_user_id").toString();

                LoginUser user = sessionWrapper.getCurrentUser(token);
                int userId = user.getLogin_user_id();
                if ( userId > -1 ) {
                    final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
                    requestContext.setSecurityContext(new SecurityContext() {
                        //重写当前请求的安全信息
                        //获取用户id: 通过 getUserPrincipal().getName() 方法
                        //获取用户redis的key: 通过 getAuthenticationScheme() 方法
                        @Override
                        public Principal getUserPrincipal() {
                            return () -> String.valueOf(userId);
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            return true;
                        }

                        @Override
                        public boolean isSecure() {
                            return currentSecurityContext.isSecure();
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return token;
                        }
                    });
                } else {
                    access = false;
                }
            } catch (Exception e) {
                access = false;
            }
        } else {
            access = false;
        }
        if ( !access ) {    //拦截
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("请先登录哦~")
                    .build());
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SessionWrapper sessionWrapper;
}
