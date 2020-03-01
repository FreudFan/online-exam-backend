package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * 用户信息
 */
@Data
public class LoginUser implements Serializable {
    @JsonIgnore
    private final String TABLE_NAME = "login_user";

    private Integer id;
    private String username;
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    /***
     * 学校
     */
    private Integer school_id;
    /***
     * 学院
     */
    private Integer college_id;
    /***
     * 专业id
     */
    private Integer major_id;
    /***
     * 班级id
     */
    private String class_id;

    @ApiModelProperty(value = "用户角色枚举{0:普通用户,1:试题管理员,2:系统管理员}, 前端需赋值, 未赋值则默认为0")
    private Integer role;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;

    @JsonIgnore
    @ApiModelProperty(value = "密保问题list")
    private List<String> question;

    @JsonIgnore
    @ApiModelProperty(value = "密保答案list")
    private List<String> answer;
}
