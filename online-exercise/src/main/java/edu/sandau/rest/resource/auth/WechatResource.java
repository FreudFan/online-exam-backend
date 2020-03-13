package edu.sandau.rest.resource.auth;

import edu.sandau.entity.User;
import edu.sandau.service.UserService;
import edu.sandau.validate.wechat.Jscode2session;
import edu.sandau.validate.wechat.WechatAppHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("auth/wechat")
@Api(value = "登录权限接口")
public class WechatResource {
    @Autowired
    private WechatAppHolder wechatAppHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession httpSession;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public User login(String code) throws Exception {
        Jscode2session jscode2session = wechatAppHolder.login(code);
        String wxId = jscode2session.getOpenid();
        httpSession.setAttribute("wxId", wxId);
        //TODO 通过wxId查询到用户
        return null;
    }

}
