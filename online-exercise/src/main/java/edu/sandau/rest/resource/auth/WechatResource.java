package edu.sandau.rest.resource.auth;

import edu.sandau.entity.User;
import edu.sandau.security.SessionUtils;
import edu.sandau.service.UserService;
import edu.sandau.validate.wechat.Jscode2session;
import edu.sandau.validate.wechat.WechatAppHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
    @Autowired
    private SessionUtils sessionUtils;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(Map<String, String> param) throws Exception {
        String code = MapUtils.getString(param, "code", null);
        if(code==null) {
            return Response.accepted().status(Response.Status.BAD_REQUEST).build();
        }
        Jscode2session jscode2session = wechatAppHolder.login(code);
        String wxId = jscode2session.getOpenid();
        httpSession.setAttribute(SessionUtils.USER_wxID_PREFIX, wxId);
        User user = userService.getUserByWxId(wxId);
        if(user == null) {
            return Response.accepted().status(Response.Status.UNAUTHORIZED).build();
        }
        sessionUtils.addUserToSession(user);
        return Response.ok(user).build();
    }

}
