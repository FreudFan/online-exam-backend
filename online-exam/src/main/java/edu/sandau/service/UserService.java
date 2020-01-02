package edu.sandau.service;

import edu.sandau.dao.LoginUserDao;
import edu.sandau.dao.LoginUserSecurityDao;
import edu.sandau.model.LoginUser;
import edu.sandau.model.LoginUserSecurity;
import edu.sandau.security.SessionWrapper;
import edu.sandau.utils.MapUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public LoginUser addUser(Map<String,Object> loginMap) throws Exception {
        LoginUser loginUser = (LoginUser) MapUtil.mapToObject(loginMap, LoginUser.class);
        LoginUserSecurity usersSecurity = (LoginUserSecurity) MapUtil.mapToObject(loginMap, LoginUserSecurity.class);
        //添加用户主表
        if ( loginUser.getRole() == null ) {
            //默认为注册用户
            loginUser.setRole(0);
        }
        if ( check(loginMap) == null ) {
            //查重
            loginUser = loginUserDao.save(loginUser);
        } else {
            return null;
        }
        //添加用户密保表
        if ( usersSecurity.getAnswer() != null && usersSecurity.getQuestion() != null
                && usersSecurity.getAnswer().size() > 0 && usersSecurity.getQuestion().size() > 0 ) {
            usersSecurity.setLogin_user_id(loginUser.getLogin_user_id());
            loginUserSecurityDao.save(usersSecurity);
        }
        //注册session
        sessionWrapper.addSessionToRedis(loginUser);
        return loginUser;
    }

    /***
     * 查询是否存在指定用户
     * @param map { username，email，telephone }
     * @return 若存在用户，返回用户
     * @throws Exception
     */
    public LoginUser check(Map<String,Object> map) throws Exception {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String username = MapUtils.getString(map, "username", null);
        if (!StringUtils.isEmpty(username)) {
            keys.add("username");
            values.add(username);
        }
        String email = MapUtils.getString(map, "email", null);
        if (!StringUtils.isEmpty(email)) {
            keys.add("email");
            values.add(email);
        }
        String telephone = MapUtils.getString(map, "telephone", null);
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
