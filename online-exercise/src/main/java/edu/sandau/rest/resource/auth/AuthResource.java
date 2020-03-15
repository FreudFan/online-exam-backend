package edu.sandau.rest.resource.auth;

import edu.sandau.entity.User;
import edu.sandau.enums.LoginValueEnum;
import edu.sandau.security.SessionUtils;
import edu.sandau.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("auth")
@Api(value = "登录权限接口")
public class AuthResource {
    @Autowired
    private UserService userService;
    @Autowired
    private SessionUtils sessionUtils;

    /***
     * 用户使用 用户名、手机号、邮箱 和 密码 登入
     * 判断 手机号：全数字 邮箱：包含'@'
     * @param user
     * @return 用户信息
     * @throws Exception
     */
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

        sessionUtils.addUserToSession(user);
        return Response.ok(user).build();
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
    @ApiOperation(value = "用户注册", response = String.class)
    @ApiResponses({
            @ApiResponse(code=400, message="用户已注册")
    })
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response register(User user) throws Exception {
        user = userService.addUser(user);
        if ( user == null ) {
            return Response.accepted("same value").status(Response.Status.BAD_REQUEST).build();
        }
        sessionUtils.addUserToSession(user);
        return Response.accepted(user).build();
    }

    /***
     * 检查是否有存在指定用户，不允许有相同用户名，邮箱，手机号
     * @param user { username，email，telephone }
     * @return 若存在，返回exist，不存在，返回null
     * @throws Exception
     */
    @ApiOperation(value = "检查是否有存在指定用户", response = String.class,
                    notes = "检查 username，email，telephone 是否唯一")
    @PUT
    @Path("check")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response check(User user) throws Exception {
        user = userService.check(user);
        if (user != null) {
            return Response.ok("exist").build();
        }
        return Response.ok().build();
    }

}
