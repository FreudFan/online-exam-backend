package authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Auth
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //获取客户端Header中提交的token
        String token = requestContext.getHeaderString("Authorization");
        //判断用户是否已登录
        if (StringUtils.isEmpty(token) || !check(token)) {
            //拦截
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("请先登录哦~")
                    .build());
        }
    }

    private boolean check(String token) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if ( redisTemplate.hasKey(token) && hashOperations.hasKey(token, "user") ) {
            httpSession.setAttribute("user", token);
            return true;
        }
        return false;
    }

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private RedisTemplate redisTemplate;

}
