package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginUser implements Serializable {
    private final String TABLE_NAME = "login_user";

    private Integer login_user_id;
    private String username;
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    private String subject_id;
    // 0:普通用户 1:试题管理员 2:系统管理员
    private Integer role;

    private Date createtime;
    private Date updatetime;
}
