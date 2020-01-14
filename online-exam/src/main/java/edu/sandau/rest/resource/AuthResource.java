package edu.sandau.rest.resource;

import edu.sandau.enums.LoginValueEnum;
import edu.sandau.rest.model.User;
import edu.sandau.rest.model.VerificationCode;
import edu.sandau.service.MessageService;
import edu.sandau.service.UserService;
import edu.sandau.security.SessionWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("auth")
@Api(value = "登录接口")
public class AuthResource {
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private UserService userService;
    @Context
    private SecurityContext securityContext;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /***
     * 用户使用 用户名、手机号、邮箱 和 密码 登入
     * 判断 手机号：全数字 邮箱：包含'@'
     * @param name
     * @param password
     * @return 用户信息
     * @throws Exception
     */
    @ApiOperation(value = "用户登录", response = Map.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名、手机号、邮箱", dataType = "String", required = true ),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true )
    })
    @ApiResponses({
            @ApiResponse(code=400, message="用户名或密码错误"),
            @ApiResponse(code=200,
                    message="token: 6a41255e-a2ba-4122-a945-d61ebc81747b\n" +
                            "会拿到一个User类和token字符串，用户登录后所有传参需在请求头加入 Authorization ,否则请求会被阻止",
                    response = User.class),
    })
    @POST
    @Path("login")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response login(@FormParam("name") String name, @FormParam("password") String password) throws Exception {
        LoginValueEnum loginValue;
        if ( name.contains("@") ) {
            //识别是否是邮箱
            loginValue = LoginValueEnum.EMAIL;
        } else if ( name.length() == 11 && NumberUtils.isDigits(name) ) {
            //识别是手机号
            loginValue = LoginValueEnum.TELEPHONE;
        } else {
            loginValue = LoginValueEnum.USERNAME;
        }
        User user = userService.login(loginValue, name, password);
        if ( user == null ) {
            return Response.accepted().status(Response.Status.BAD_REQUEST).build();
        }

        String token = sessionWrapper.addSessionToRedis(user);
        user.setToken(token);
        Map<String, Object> param = new HashMap<>(2);
        param.put("user", user);
        param.put("token", token);

        return Response.ok(param).build();
    }

    /***
     * 用户注册，前端需为role赋值{0,1,2}，未赋值则默认为0，即普通用户
     * 注：用户名（username）不允许包含'@' 不允许全数字
     * 注：邮箱（email） 必须包含'@'
     * 注：手机号只允许中国大陆手机号，只允许全数字
     * @param user
     * @return 用户信息（脱敏）
     * @throws Exception
     */
    @POST
    @Path("register")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response register(User user) throws Exception {
        user = userService.addUser(user);
        if ( user == null ) {
            return Response.accepted("same value").status(Response.Status.BAD_REQUEST).build();
        }
        return Response.accepted(user).build();
    }

    /***
     * 检查是否有存在指定用户，不允许有相同用户名，邮箱，手机号
     * @param user { username，email，telephone }
     * @return 若存在，返回exist，不存在，返回null
     * @throws Exception
     */
    @PUT
    @Path("check")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response check(User user) throws Exception {
        user = userService.check(user);
        if (user != null) {
            return Response.ok("exist").build();
        }
        return Response.ok().build();
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
    @POST
    @Path("code")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getVerificationCode(VerificationCode code) throws Exception {
        //短信、邮件地址
        String to = code.getTo();
        //邮件：0， 短信：1
        Integer type = code.getType();
        String uuid = null;
        if ( !StringUtils.isEmpty(to) ) {
            switch (type){
                case 0:
                    //sendEmail
                    uuid = messageService.sendEmailVerification(to);
                    break;
                case 1:
                    //sendMessage

                    break;
                default:
                    break;
            }
        }
        if ( uuid != null ) {
            return Response.accepted(uuid).build();
        }
        return Response.accepted(false).status(500).build();
    }

    @POST
    @Path("check-code")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response checkVerificationCode(VerificationCode verificationCode) throws Exception {
        String code = redisTemplate.opsForValue().get(verificationCode.getKey());
        if ( code != null && code.equals(verificationCode.getCode())) {
            return Response.accepted(true).build();
        }
        return Response.accepted(false).status(500).build();
    }

}
