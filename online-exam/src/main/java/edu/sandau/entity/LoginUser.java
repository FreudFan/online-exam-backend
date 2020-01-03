package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class LoginUser implements Serializable {
    @JSONField(serialize = false)
    private final String TABLE_NAME = "login_user";

    @Getter @Setter
    private Integer login_user_id;
    @Getter @Setter
    private String username;
    @Getter @Setter
    @JSONField(serialize = false)
    private String password;
    @Getter @Setter
    private String realname;
    @Getter @Setter
    private Integer gender;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String telephone;
    @Getter @Setter
    private String subject_id;
    // 0:普通用户 1:试题管理员 2:系统管理员
    @Getter @Setter
    private Integer role;

    @Getter @JSONField(serialize = false)
    private Date createtime;
    @Getter @JSONField(serialize = false)
    private Date updatetime;
}
