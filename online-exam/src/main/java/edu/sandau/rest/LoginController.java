package edu.sandau.rest;

import com.alibaba.fastjson.JSONObject;
import edu.sandau.model.LoginUser;
import edu.sandau.service.UserService;
import edu.sandau.security.SessionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("user")
public class LoginController {

    @Autowired
    SessionWrapper sessionWrapper;

    @Autowired
    private UserService userService;

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
    public ResponseEntity login(@FormParam("name") String name, @FormParam("password") String password) throws Exception {
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
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

        String token = sessionWrapper.addSessionToRedis(loginUser);
        Map<String, Object> param = new HashMap<>();
        param.put("user", loginUser);
        param.put("token", token);
        return new ResponseEntity<>(param, HttpStatus.OK);
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
    public ResponseEntity register(Map<String,Object> map) throws Exception {
        LoginUser loginUser = userService.addUser(map);
        if ( loginUser == null ) {
            return new ResponseEntity<>("same value", HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(loginUser, HttpStatus.OK);
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
    public ResponseEntity check(Map<String,Object> map) throws Exception {
        if ( map.containsKey("username") || map.containsKey("email") || map.containsKey("telephone") ) {
            LoginUser user = userService.check(map);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
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
    public ResponseEntity resetPassword(@FormParam("id") Integer id, @FormParam("password") String password) throws Exception {
        boolean ok = userService.resetPassword(id, password);
        if ( ok ) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
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
    public ResponseEntity getSecurityQuestion(Map<String,Object> map) throws Exception {
        Integer id = MapUtils.getInteger(map,"id", null);
        if ( id != null ) {
            List<String> questions = userService.getSecurityQuestion(id);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
    }

}
