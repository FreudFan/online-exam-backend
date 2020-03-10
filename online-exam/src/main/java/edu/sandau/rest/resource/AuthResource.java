package edu.sandau.rest.resource;

import edu.sandau.entity.LoginUser;
import edu.sandau.enums.LoginValueEnum;
import edu.sandau.rest.model.User;
import edu.sandau.rest.model.UserSecurity;
import edu.sandau.rest.model.VerificationCode;
import edu.sandau.service.MessageService;
import edu.sandau.service.UserService;
import edu.sandau.security.SessionWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("auth")
@Api(value = "登录权限接口")
public class AuthResource {
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /***
     * 用户使用 用户名、手机号、邮箱 和 密码 登入
     * 判断 手机号：全数字 邮箱：包含'@'
     * @param user
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
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(User user) throws Exception {
        String name = user.getName();
        String password = user.getPassword();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            return Response.accepted().status(Response.Status.BAD_REQUEST).build();
        }
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
        user = userService.login(loginValue, name, password);
        if ( user == null ) {
            return Response.accepted().status(Response.Status.BAD_REQUEST).build();
        }

        String token = sessionWrapper.addSessionToRedis(user);
        user.setToken(token);

        return Response.ok(user).build();
    }

    /***
     * 用户注册，前端需为role赋值{0,1,2}，未赋值则默认为0，即普通用户
     * 注：用户名（username）不允许包含'@' 不允许全数字
     * 注：邮箱（email） 必须包含'@'
     * 注：手机号只允许中国大陆手机号，只允许全数字
     * @param loginUser
     * @return 用户信息（脱敏）
     * @throws Exception
     */
    @ApiOperation(value = "用户注册", response = String.class)
    @ApiResponses({
            @ApiResponse(code=400, message="用户已注册")
    })
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response register(LoginUser loginUser) throws Exception {
        User user = userService.addUser(loginUser);
        if ( user == null ) {
            return Response.accepted("same value").status(Response.Status.BAD_REQUEST).build();
        }
        return Response.accepted(user).build();
    }

    /***
     * 检查是否有存在指定用户，不允许有相同用户名，邮箱，手机号
     * @param loginUser { username，email，telephone }
     * @return 若存在，返回exist，不存在，返回null
     * @throws Exception
     */
    @ApiOperation(value = "检查是否有存在指定用户", response = String.class,
                    notes = "检查 username，email，telephone 是否唯一")
    @PUT
    @Path("check")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response check(LoginUser loginUser) throws Exception {
        loginUser = userService.check(loginUser);
        if (loginUser != null) {
            return Response.ok("exist").build();
        }
        return Response.ok().build();
    }

    /***
     * 获取用户密保问题
     * @param id  用户id
     * @return 密保问题list
     * @throws Exception
     */
    @ApiOperation(value = "获取用户密保问题", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "Integer", required = true )
    })
    @ApiResponses({
            @ApiResponse(code=500, message="参数不合规"),
            @ApiResponse(code=200, message="用户问题列表", response = List.class),
    })
    @GET
    @Path("security-question")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSecurityQuestion(@QueryParam("id") Integer id) throws Exception {
        if ( id != null ) {
            List<Map<String,Object>> questions = userService.getSecurityQuestion(id);
            return Response.ok(questions).build();
        }
        return Response.accepted(false).status(500).build();
    }

    @ApiOperation(value = "核对密保答案", response = Boolean.class)
    @POST
    @Path("check-question")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkSecurityQuestion(UserSecurity userSecurity) throws Exception {
        boolean ok = userService.checkSecurityQuestion(userSecurity.getId(), userSecurity.getAnswer());
        return Response.accepted(ok).build();
    }

    /***
     * 发邮件、短信验证
     *  to 短信、邮件
     *  type 邮件：0， 短信：1
     * @return
     */
    @ApiOperation(value = "发邮件、短信验证", response = String.class)
    @ApiResponses({
            @ApiResponse(code=500, message="发送失败"),
            @ApiResponse(code=200, message="验证码id，校验验证码时需传此id", response = String.class)
    })
    @POST
    @Path("code")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
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

    @ApiOperation(value = "验证码校验", response = Boolean.class)
    @POST
    @Path("check-code")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkVerificationCode(VerificationCode verificationCode) throws Exception {
        String code = redisTemplate.opsForValue().get(verificationCode.getKey());
        if ( code != null && code.equals(verificationCode.getCode())) {
            return Response.accepted(true).build();
        }
        return Response.accepted(false).status(500).build();
    }

}
