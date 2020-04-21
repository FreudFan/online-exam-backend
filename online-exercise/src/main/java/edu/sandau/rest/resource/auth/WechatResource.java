package edu.sandau.rest.resource.auth;

import edu.sandau.entity.User;
import edu.sandau.security.Auth;
import edu.sandau.security.SessionUtils;
import edu.sandau.service.UserService;
import edu.sandau.validate.wechat.JsCode2Session;
import edu.sandau.validate.wechat.WechatAppHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Auth
@Path("auth/wechat")
@Api(value = "登录权限接口")
public class WechatResource {
    @Autowired
    private WechatAppHolder wechatAppHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionUtils sessionUtils;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(Map<String, String> param) throws Exception {
        String code = MapUtils.getString(param, "code", null);
        if (code == null) {
            return Response.accepted().status(Response.Status.BAD_REQUEST).build();
        }
        JsCode2Session jscode2session = wechatAppHolder.login(code);
        String wxId = jscode2session.getOpenid();
        sessionUtils.setAttribute(SessionUtils.USER_wxID_PREFIX, wxId);
        User user = userService.getUserByWxId(wxId);
        Map<String, Object> params = new HashMap<>(2);
        params.put("wxId", wxId);
        if (user == null) {
            params.put("msg", "用户未注册~");
            return Response.accepted(params).status(Response.Status.UNAUTHORIZED).build();
        }
        params.put("user", user);
        sessionUtils.addUserToSession(user);
        return Response.ok(params).build();
    }

}
