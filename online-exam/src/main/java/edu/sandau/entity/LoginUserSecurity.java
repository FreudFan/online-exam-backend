package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class LoginUserSecurity {
    @JSONField(serialize = false)
    private final String TABLE_NAME = "login_user_security";
    @Getter @Setter
    private Integer login_user_security_id;
    @Getter @Setter
    private Integer login_user_id;
    @Getter @Setter
    private List<String> question;
    @Getter @Setter
    private List<String> answer;

    @Getter @JSONField(serialize = false)
    private Date createtime;
    @Getter @JSONField(serialize = false)
    private Date updatetime;
}
