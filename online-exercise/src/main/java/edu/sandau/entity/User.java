package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * 用户信息
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String realname;
    private Integer gender;
    private String email;
    private String telephone;
    @ApiModelProperty(value = "用户角色枚举{0:普通用户,1:试题管理员,2:系统管理员}, 前端需赋值, 未赋值则默认为0")
    private Integer role;
    private String wxId;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;

    //登录时使用
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;
}
