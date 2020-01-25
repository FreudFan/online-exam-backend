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
import edu.sandau.service.OrganizationService;
import edu.sandau.service.SysEnumService;
import edu.sandau.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private LoginUserDao loginUserDao;
    @Autowired
    private LoginUserSecurityDao loginUserSecurityDao;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private SysEnumService enumService;
    @Autowired
    private OrganizationService organizationService;

    public User refactorEntity(LoginUser loginUser) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(loginUser, user);
        int gender_id = loginUser.getGender();
        String gender = enumService.getEnumName("COMMON", "GENDER", gender_id);
        user.setGender(gender);
        String school = organizationService.getOrgById(loginUser.getSchool_id()).getName();
        user.setSchool(school);
        String college = organizationService.getOrgById(loginUser.getCollege_id()).getName();
        user.setCollege(college);
        String major = organizationService.getOrgById(loginUser.getMajor_id()).getName();
        user.setMajor(major);
        return user;
    }

    @Override
    public User addUser(LoginUser loginUser) throws Exception {
        LoginUserSecurity userSecurity = new LoginUserSecurity();
        userSecurity.setQuestion(loginUser.getQuestion());
        userSecurity.setAnswer(loginUser.getAnswer());
        if ( loginUser.getRole() == null ) {
            //默认为注册用户
            loginUser.setRole(RoleTypeEnum.NORMAL_USER.getValue());
        }
        if ( this.check(loginUser) == null ) {
            //添加用户主表
            loginUser = loginUserDao.save(loginUser);
        } else {
            return null;
        }
        //添加用户密保表
        userSecurity.setLogin_user_id(loginUser.getId());
        loginUserSecurityDao.save(userSecurity);
        loginUser.setId(loginUser.getId());
        User user = this.refactorEntity(loginUser);
        //注册session
        String token = sessionWrapper.addSessionToRedis(user);
        user.setToken(token);
        return user;
    }

    @Override
    public LoginUser check(LoginUser loginUser) throws Exception {
        List<LoginUser> loginUsers = this.checkList(loginUser);
        if ( loginUsers != null ) {
            loginUser = loginUsers.get(0);
            return loginUser;
        }
        return null;
    }

    @Override
    public Integer checkNumber(LoginUser user) throws Exception {
        List<LoginUser> loginUsers = this.checkList(user);
        return loginUsers.size();
    }

    /***
     * 检测 用户名、邮件、电话 相同用户列表
     * @param user
     * @return
     * @throws Exception
     */
    private List<LoginUser> checkList(LoginUser user) throws Exception {
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
        return this.refactorEntity(loginUser);
    }

    @Override
    public Boolean resetPassword(Integer id, String password) throws Exception {
        return loginUserDao.updateUserById(id, "password", password);
    }

    @Override
    public Boolean resetPassword(Integer id) throws Exception {
        String defalutPassword = enumService.getEnumName("COMMON", "DEFAULT_PASSWORD", 1);
        return loginUserDao.updateUserById(id, "password", defalutPassword);
    }

    @Override
    public List<Map<String,Object>> getSecurityQuestion(Integer id) throws Exception {
        return loginUserSecurityDao.getQuestionById(id);
    }

    @Override
    public Boolean checkSecurityQuestion(Integer id, String answer) throws Exception {
        int count = loginUserSecurityDao.checkAnswerById(id, answer);
        return count != 0;
    }

    @Override
    public Page getUsersByPage(Page page) throws Exception {
        List<LoginUser> loginUsers = loginUserDao.listUserByPage(page);
        int total = loginUserDao.getCount();
        List<User> users = new ArrayList<>();
        for (LoginUser loginUser: loginUsers) {
            User user = this.refactorEntity(loginUser);
            users.add(user);
        }
        page.setRows(users);
        page.setTotal(total);
        return page;
    }

    @Override
    public User updateUser(LoginUser loginUser) throws Exception {
        if (this.checkNumber(loginUser) >= 2) {
            return null;
        } else {
            loginUserDao.update(loginUser);
            loginUser = loginUserDao.getUserById(loginUser.getId());
        }
        return this.refactorEntity(loginUser);
    }

}
