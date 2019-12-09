package model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginUsers implements Serializable {
    @JSONField(serialize = false)
    @Getter private final String TABLE_NAME = "login_users";

    private Integer login_users_id;
    private String username;
    @JSONField(serialize = false)
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    private String subject_id;
    private Integer role;   // 0:普通用户 1:试题管理员 2:系统管理员

    @JSONField(serialize = false)
    private @Getter Date createtime;
    @JSONField(serialize = false)
    private @Getter Date updatetime;
}
