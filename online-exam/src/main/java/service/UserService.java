package service;

import dao.UserDao;
import model.LoginUsers;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public LoginUsers addUser(LoginUsers loginUsers) throws Exception {
        if ( loginUsers.getRole() == null ) {
            loginUsers.setRole(0);  //默认为注册用户
        }
        Map<String,String> map = new HashMap<>();
        map.put("username", loginUsers.getUsername());
        map.put("email", loginUsers.getEmail());
        map.put("telephone", loginUsers.getTelephone());
        loginUsers = userDao.save(loginUsers);
        if ( this.check(map) ) {
            loginUsers = userDao.save(loginUsers);
        } else {
            return null;
        }

//        if ( userDao.getUserByRealname(loginUsers.getRealname()).size() <= 0 ) {
//            loginUsers = userDao.save(loginUsers);
//        } else {
//            return null;
//        }
        return loginUsers;
    }

    /***
     * 查重
     * @param map { username，email，telephone }
     * @return true 无重复 | false 有重复
     */
    public Boolean check(Map<String,String> map) throws Exception {
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
        return userDao.checkSameValue(keys, values);
    }

}
