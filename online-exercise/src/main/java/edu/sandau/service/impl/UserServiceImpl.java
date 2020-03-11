package edu.sandau.service.impl;

import edu.sandau.dao.UserDao;
import edu.sandau.entity.User;
import edu.sandau.enums.LoginValueEnum;
import edu.sandau.enums.RoleTypeEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.service.SysEnumService;
import edu.sandau.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysEnumService enumService;

    @Override
    public User refactorEntity(User user) {
//        User user = new User();
//        BeanUtils.copyProperties(loginUser, user);
//        int gender_id = loginUser.getGender();
//        String genderName = enumService.getEnumName("COMMON", "GENDER", gender_id);
//        user.setGenderName(genderName);
//        String school = organizationService.getOrgById(loginUser.getSchool_id()).getName();
//        user.setSchool(school);
//        String college = organizationService.getOrgById(loginUser.getCollege_id()).getName();
//        user.setCollege(college);
//        String major = organizationService.getOrgById(loginUser.getMajor_id()).getName();
//        user.setMajor(major);
        return user;
    }

    @Override
    public User addUser(User user) throws Exception {
        if ( user.getRole() == null ) {
            //默认为注册用户
            user.setRole(RoleTypeEnum.NORMAL_USER.getValue());
        }
        if ( this.check(user) == null ) {
            //添加用户主表
            user = userDao.save(user);
        } else {
            return null;
        }
        user.setId(user.getId());
        return user;
    }

    @Override
    public User check(User user) throws Exception {
        List<User> users = this.checkList(user);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public Integer checkNumber(User user) throws Exception {
        List<User> users = this.checkList(user);
        return users.size();
    }

    /***
     * 检测 用户名、邮件、电话 相同用户列表
     * @param user
     * @return
     * @throws Exception
     */
    private List<User> checkList(User user) throws Exception {
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
        return userDao.getUserByFields(keys, values);
    }

    @Override
    public User login(LoginValueEnum loginValue, String loginName, String password) throws  Exception {
        User user = userDao.login(loginValue.getName(),loginName,password);
        if ( user == null ) {
            return null;
        }
        return this.refactorEntity(user);
    }

    @Override
    public Boolean resetPassword(Integer id, String password) throws Exception {
        return userDao.updateUserById(id, "password", password);
    }

    @Override
    public Boolean resetPassword(Integer id) throws Exception {
        String defalutPassword = enumService.getEnumName("COMMON", "DEFAULT_PASSWORD", 1);
        return userDao.updateUserById(id, "password", defalutPassword);
    }

    @Override
    public Page getUsersByPage(Page page) throws Exception {
        List<User> users = userDao.listUserByPage(page);
        int total = userDao.getCount();
        page.setRows(users);
        page.setTotal(total);
        return page;
    }

    @Override
    public User updateUser(User user) throws Exception {
        if (this.checkNumber(user) >= 2) {
            return null;
        } else {
            userDao.update(user);
            user = userDao.getUserById(user.getId());
        }
        return this.refactorEntity(user);
    }

}
