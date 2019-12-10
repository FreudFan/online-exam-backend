package service;

import dao.LoginUsersDao;
import dao.LoginUsersSecurityDao;
import model.LoginUsers;
import model.LoginUsersSecurity;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.CommonsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    private LoginUsersDao loginUsersDao;
    @Autowired
    private LoginUsersSecurityDao loginUsersSecurityDao;

    public LoginUsers addUser(Map<String,Object> loginMap) throws Exception {
        LoginUsers loginUsers = (LoginUsers)CommonsUtils.mapToObject(loginMap, LoginUsers.class);
        LoginUsersSecurity usersSecurity = (LoginUsersSecurity)CommonsUtils.mapToObject(loginMap, LoginUsersSecurity.class);
        //添加用户主表
        if ( loginUsers.getRole() == null ) {
            loginUsers.setRole(0);  //默认为注册用户
        }
        if ( check(loginMap) == null ) { //查重
            loginUsers = loginUsersDao.save(loginUsers);
        } else {
            return null;
        }
        //添加用户密保表
        if ( usersSecurity.getAnswer() != null && usersSecurity.getQuestion() != null
                && usersSecurity.getAnswer().size() > 0 && usersSecurity.getQuestion().size() > 0 ) {
            usersSecurity.setLogin_users_id(loginUsers.getLogin_users_id());
            loginUsersSecurityDao.save(usersSecurity);
        }

        return loginUsers;
    }

    /***
     * 查询是否存在指定用户
     * @param map { username，email，telephone }
     * @return 若存在用户，返回用户
     * @throws Exception
     */
    public LoginUsers check(Map<String,Object> map) throws Exception {
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
        return loginUsersDao.getUserByFields(keys, values);
    }

    public LoginUsers login(String loginValue, String loginNmae, String password) throws  Exception {
        return loginUsersDao.login(loginValue,loginNmae,password);
    }

    public boolean resetPassword(Integer id, String password) throws Exception {
        return loginUsersDao.updateUserById(id, "password", password);
    }

}
