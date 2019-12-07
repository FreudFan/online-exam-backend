package model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class LoginUsers implements Serializable {
    @JSONField(serialize = false)
    private @Getter final String TABLE_NAME = "login_users";

    private Integer login_users_id;
    private String username;
    @JSONField(serialize = false)
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    private String subject_id;
    private Integer role;   // 0:试题管理员 1:试题管理员 2:系统管理员

}
