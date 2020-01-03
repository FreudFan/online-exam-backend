package edu.sandau.rest.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class User {
    private Integer id;
    private String token;
    private String username;
    @JSONField(serialize = false)
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    private String subject_id;
    private Integer role;
    @JSONField(serialize = false)
    private List<String> question;
    @JSONField(serialize = false)
    private List<String> answer;
}
