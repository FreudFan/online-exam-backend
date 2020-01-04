package edu.sandau.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class User {
    private Integer id;
    private String token;
    private String username;
    @JsonIgnore
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    private String subject_id;
    private Integer role;
    @JsonIgnore
    private List<String> question;
    @JsonIgnore
    private List<String> answer;
}
