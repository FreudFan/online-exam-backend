package edu.sandau.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoginUserSecurity {
    private final String TABLE_NAME = "login_user_security";

    private Integer id;
    private Integer login_user_id;
    private List<String> question;
    private List<String> answer;

    private Date createtime;
    private Date updatetime;
}
