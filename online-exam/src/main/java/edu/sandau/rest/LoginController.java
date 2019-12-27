package edu.sandau.rest;

import edu.sandau.model.LoginUser;
import edu.sandau.service.MessageService;
import edu.sandau.service.UserService;
import edu.sandau.security.SessionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("user")
public class LoginController {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private UserService userService;
    @Context
    private SecurityContext securityContext;
    @Autowired
    private MessageService messageService;

    /***
     * 用户使用 用户名、手机号、邮箱 和 密码 登入
     * 判断 手机号：全数字 邮箱：包含'@'
     * @param name
     * @param password
     * @return 用户信息
     * @throws Exception
     */
    @POST
    @Path("login")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response login(@FormParam("name") String name, @FormParam("password") String password) throws Exception {
        String loginValue;
        if ( name.contains("@") ) { //识别是否是邮箱
            loginValue = "email";
        } else if ( name.length() == 11 && NumberUtils.isDigits(name) ) {   //识别是手机号
            loginValue = "telephone";
        } else {
            loginValue = "username";
        }
        LoginUser loginUser = userService.login(loginValue, name, password);
        if ( loginUser == null ) {
            return Response.accepted().status(HttpStatus.BAD_REQUEST.value()).build();
        }

        String token = sessionWrapper.addSessionToRedis(loginUser);
        Map<String, Object> param = new HashMap<>();
        param.put("user", loginUser);
        param.put("token", token);

        return Response.ok(param).build();
    }

    /***
     * 用户注册，前端需为role赋值{0,1,2}，未赋值则默认为0，即普通用户
     * 注：用户名（username）不允许包含'@' 不允许全数字
     * 注：邮箱（email） 必须包含'@'
     * 注：手机号只允许中国大陆手机号，只允许全数字
     * @param map
     * @return 用户信息（脱敏）
     * @throws Exception
     */
    @POST
    @Path("register")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response register(Map<String,Object> map) throws Exception {
        LoginUser loginUser = userService.addUser(map);
        if ( loginUser == null ) {
            return Response.accepted("same value").status(HttpStatus.BAD_REQUEST.value()).build();
        }
        return Response.accepted(loginUser).build();
    }

    /***
     * 检查是否有存在指定用户，不允许有相同用户名，邮箱，手机号
     * @param map { username，email，telephone }
     * @return 若存在，返回用户，不存在，返回null
     * @throws Exception
     */
    @PUT
    @Path("check")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response check(Map<String,Object> map) throws Exception {
        if ( map.containsKey("username") || map.containsKey("email") || map.containsKey("telephone") ) {
            LoginUser user = userService.check(map);
            return Response.ok().build();
        }
        return Response.accepted().status(500).build();
    }

    /***
     * 重置密码
     * @param id 用户id
     * @param password 用户新密码
     * @return
     * @throws Exception
     */
    @POST
    @Path("reset-password")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response resetPassword(@FormParam("id") Integer id, @FormParam("password") String password) throws Exception {
        boolean ok = userService.resetPassword(id, password);
        if ( ok ) {
            return Response.ok(true).build();
        }
        return Response.accepted(false).status(500).build();
    }

    /***
     * 获取用户密保问题
     * @param map 需有参数 id
     * @return 问题list
     * @throws Exception
     */
    @PUT
    @Path("security-question")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getSecurityQuestion(Map<String,Object> map) throws Exception {
        Integer id = MapUtils.getInteger(map,"id", null);
        if ( id != null ) {
            List<String> questions = userService.getSecurityQuestion(id);
            return Response.ok(questions).build();
        }
        return Response.accepted(false).status(500).build();
    }

    /***
     * 发邮件、短信验证
     *  to 短信、邮件
     *  type 邮件：0， 短信：1
     * @return
     */
    @PUT
    @Path("verification-code")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getVerificationCode(Map<String,Object> map) throws Exception {
        String to = MapUtils.getString(map, "to", null); //短信、邮件地址
        Integer type = MapUtils.getInteger(map, "type", null);  //邮件：0， 短信：1
        String uuid = null;
        if ( !StringUtils.isEmpty(to) ) {
            switch (type){
                case 0: //sendEmail
                    uuid = messageService.sendEmailVerification(to);
                    break;
                case 1: //sendMessage

                    break;
            }
        }
        if ( uuid != null ) {
            return Response.accepted(uuid).build();
        }
        return Response.accepted(false).status(500).build();
    }

}
