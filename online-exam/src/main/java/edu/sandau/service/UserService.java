package edu.sandau.service;

import edu.sandau.enums.LoginValueEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user) throws Exception;

    /***
     * 查询是否存在指定用户
     * @param user { username，email，telephone }
     * @return 若存在用户，返回用户
     * @throws Exception
     */
    User check(User user) throws Exception;

    /***
     * 若登录失败，返回null
     * @param loginValue
     * @param loginNmae
     * @param password
     * @return
     * @throws Exception
     */
    User login(LoginValueEnum loginValue, String loginNmae, String password) throws  Exception;

    Boolean resetPassword(Integer id, String password) throws Exception;

    List<String> getSecurityQuestion(Integer id) throws Exception;

    /***
     * 分页查询所有用户
     * @param page
     * @return
     * @throws Exception
     */
    Page getUsersByPage(Page page) throws Exception;

}
