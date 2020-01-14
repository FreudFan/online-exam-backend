package edu.sandau.rest.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "用户")
@Data
public class User {
    private Integer id;

    @ApiModelProperty(value = "session id")
    private String token;

    @ApiModelProperty(value = "用户名 唯一")
    private String username;

    @JSONField(serialize = false)
    private String password;

    @ApiModelProperty(value = "用户真实姓名")
    private String realname;

    @ApiModelProperty(value = "性别：{0:男,1:女}")
    private Integer gender;

    @ApiModelProperty(value = "邮件 唯一")
    private String email;

    @ApiModelProperty(value = "手机号 唯一")
    private String telephone;

    @ApiModelProperty(value = "组织")
    private String organization;

    @ApiModelProperty(value = "专业id")
    private String major_id;

    @ApiModelProperty(value = "班级id")
    private String class_id;

    @ApiModelProperty(value = "用户角色枚举{0:普通用户,1:试题管理员,2:系统管理员}, 前端需赋值, 未赋值则默认为0")
    private Integer role;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "密保问题list")
    private List<String> question;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "密保答案list")
    private List<String> answer;
}
