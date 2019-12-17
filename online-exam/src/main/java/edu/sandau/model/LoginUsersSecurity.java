package edu.sandau.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class LoginUsersSecurity {
    @JSONField(serialize = false)
    @Getter private final String TABLE_NAME = "login_users_security";

    private Integer login_users_security_id;
    private Integer login_users_id;
    private List<String> question;
    private List<String> answer;
}
