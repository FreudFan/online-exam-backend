package edu.sandau.service;

import edu.sandau.dao.LoginUserDao;
import edu.sandau.dao.LoginUserSecurityDao;
import edu.sandau.entity.LoginUser;
import edu.sandau.entity.LoginUserSecurity;
import edu.sandau.rest.model.User;
import edu.sandau.security.SessionWrapper;
import edu.sandau.utils.MapUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    private LoginUserDao loginUserDao;
    @Autowired
    private LoginUserSecurityDao loginUserSecurityDao;
    @Autowired
    private SessionWrapper sessionWrapper;

    public User addUser(User user) throws Exception {
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        LoginUserSecurity userSecurity = new LoginUserSecurity();
        BeanUtils.copyProperties(user, userSecurity);
        if ( loginUser.getRole() == null ) {
            //默认为注册用户
            loginUser.setRole(0);
        }
        if ( this.check(user) == null ) {
            //添加用户主表
            loginUser = loginUserDao.save(loginUser);
        } else {
            return null;
        }
        //添加用户密保表
        userSecurity.setLogin_user_id(loginUser.getLogin_user_id());
        loginUserSecurityDao.save(userSecurity);
        //注册session
        String token = sessionWrapper.addSessionToRedis(loginUser);
        user.setToken(token);
        user.setId(loginUser.getLogin_user_id());
        return user;
    }

    /***
     * 查询是否存在指定用户
     * @param user { username，email，telephone }
     * @return 若存在用户，返回用户
     * @throws Exception
     */
    public LoginUser check(User user) throws Exception {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String username = user.getUsername();
        if (!StringUtils.isEmpty(username)) {
            keys.add("username");
            values.add(username);
        }
        String email = user.getEmail();
        if (!StringUtils.isEmpty(email)) {
            keys.add("email");
            values.add(email);
        }
        String telephone = user.getTelephone();
        if (!StringUtils.isEmpty(telephone)) {
            keys.add("telephone");
            values.add(telephone);
        }
        return loginUserDao.getUserByFields(keys, values);
    }

    /***
     * 若登录失败，返回null
     * @param loginValue
     * @param loginNmae
     * @param password
     * @return
     * @throws Exception
     */
    public LoginUser login(String loginValue, String loginNmae, String password) throws  Exception {
        return loginUserDao.login(loginValue,loginNmae,password);
    }

    public boolean resetPassword(Integer id, String password) throws Exception {
        return loginUserDao.updateUserById(id, "password", password);
    }

    public List<String> getSecurityQuestion(Integer id) throws Exception {
        return loginUserSecurityDao.getQuestionById(id);
    }

}
