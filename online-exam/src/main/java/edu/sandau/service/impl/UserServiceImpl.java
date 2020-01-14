package edu.sandau.service.impl;

import edu.sandau.dao.LoginUserDao;
import edu.sandau.enums.LoginValueEnum;
import edu.sandau.enums.RoleTypeEnum;
import edu.sandau.dao.LoginUserSecurityDao;
import edu.sandau.entity.LoginUser;
import edu.sandau.entity.LoginUserSecurity;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.User;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private LoginUserDao loginUserDao;
    @Autowired
    private LoginUserSecurityDao loginUserSecurityDao;
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    public User addUser(User user) throws Exception {
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        LoginUserSecurity userSecurity = new LoginUserSecurity();
        BeanUtils.copyProperties(user, userSecurity);
        if ( loginUser.getRole() == null ) {
            //默认为注册用户
            loginUser.setRole(RoleTypeEnum.NORMAL_USER.getValue());
        }
        if ( this.check(user) == null ) {
            //添加用户主表
            loginUser = loginUserDao.save(loginUser);
        } else {
            return null;
        }
        //添加用户密保表
        userSecurity.setLogin_user_id(loginUser.getId());
        loginUserSecurityDao.save(userSecurity);
        user.setId(loginUser.getId());
        //注册session
        String token = sessionWrapper.addSessionToRedis(user);
        user.setToken(token);
        return user;
    }

    @Override
    public User check(User user) throws Exception {
        List<LoginUser> loginUsers = this.checkList(user);
        if ( loginUsers != null ) {
            LoginUser loginUser = loginUsers.get(0);
            BeanUtils.copyProperties(loginUser, user);
            return user;
        }
        return null;
    }

    @Override
    public Integer checkNumber(User user) throws Exception {
        List<LoginUser> loginUsers = this.checkList(user);
        return loginUsers.size();
    }

    private List<LoginUser> checkList(User user) throws Exception {
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

    @Override
    public User login(LoginValueEnum loginValue, String loginNmae, String password) throws  Exception {
        LoginUser loginUser = loginUserDao.login(loginValue.getName(),loginNmae,password);
        if ( loginUser == null ) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(loginUser, user);
        return user;
    }

    @Override
    public Boolean resetPassword(Integer id, String password) throws Exception {
        return loginUserDao.updateUserById(id, "password", password);
    }

    @Override
    public List<String> getSecurityQuestion(Integer id) throws Exception {
        return loginUserSecurityDao.getQuestionById(id);
    }

    @Override
    public Page getUsersByPage(Page page) throws Exception {
        List<User> users = loginUserDao.listUserByPage(page);
        int total = loginUserDao.getCount();
        page.setRows(users);
        page.setTotal(total);
        return page;
    }

    @Override
    public User updateUser(User user) throws Exception {
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        if (this.checkNumber(user) >= 2) {
            return null;
        } else {
            loginUserDao.update(loginUser);
            loginUser = loginUserDao.getUserById(user.getId());
        }
        BeanUtils.copyProperties(loginUser, user);
        return user;
    }

}
