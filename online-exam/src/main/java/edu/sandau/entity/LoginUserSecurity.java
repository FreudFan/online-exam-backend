package edu.sandau.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/***
 * 用户密保
 */
@Data
public class LoginUserSecurity {
    private final String TABLE_NAME = "login_user_security";

    private Integer id;
    /***
     * 用户id
     */
    private Integer login_user_id;
    /***
     * 密保问题List
     */
    private List<String> question;
    /***
     * 密保答案List
     */
    private List<String> answer;

    private Date createtime;
    private Date updatetime;
}
