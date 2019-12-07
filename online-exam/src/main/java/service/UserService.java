package service;

import dao.UserDao;
import model.LoginUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public LoginUsers addUser(LoginUsers loginUsers) {
        if ( loginUsers.getRole() == null ) {
            loginUsers.setRole(0);  //默认为注册用户
        }
        if ( userDao.getUserByRealname(loginUsers.getRealname()).size() == 0 ) {
            int userId = userDao.addUser(loginUsers);
        }
        return null;
    }

}
