package edu.sandau.service;

import edu.sandau.entity.LoginUser;
import edu.sandau.enums.LoginValueEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    /***
     * LoginUser 转成 User
     * @param loginUser
     * @return
     */
    User refactorEntity(LoginUser loginUser);

    /***
     * 添加用户
     * @param loginUser
     * @return
     * @throws Exception
     */
    User addUser(LoginUser loginUser) throws Exception;

    /***
     * 查询是否存在指定用户
     * @param loginUser { username，email，telephone }
     * @return 若存在用户，返回用户
     * @throws Exception
     */
    LoginUser check(LoginUser loginUser) throws Exception;

    /***
     * 查询是否存在相同用户个数
     * @param user
     * @return
     * @throws Exception
     */
    Integer checkNumber(LoginUser user) throws Exception;

    /***
     * 若登录失败，返回null
     * @param loginValue
     * @param loginNmae
     * @param password
     * @return
     * @throws Exception
     */
    User login(LoginValueEnum loginValue, String loginNmae, String password) throws  Exception;

    /***
     * 重置密码
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    Boolean resetPassword(Integer id, String password) throws Exception;

    /***
     * 设置默认密码
     * @param id
     * @return
     * @throws Exception
     */
    Boolean resetPassword(Integer id) throws Exception;

    /***
     * 获取密保问题
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getSecurityQuestion(Integer id) throws Exception;

    /***
     * 检查密保答案
     * @param id
     * @param answer
     * @return
     * @throws Exception
     */
    Boolean checkSecurityQuestion(Integer id, String answer) throws Exception;

    /***
     * 分页查询所有用户
     * @param page
     * @return
     * @throws Exception
     */
    Page getUsersByPage(Page page) throws Exception;

    /***
     * 更新用户信息
     * @param user
     * @return
     * @throws Exception
     */
    User updateUser(LoginUser user) throws Exception;

}
