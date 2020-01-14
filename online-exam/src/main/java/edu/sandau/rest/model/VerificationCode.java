package edu.sandau.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VerificationCode {
    /***
     * 验证码目标用户（邮件或手机号）
     */
    @ApiModelProperty(value = "邮件或手机号")
    private String to;
    /***
     * 验证码类型：邮件：0， 短信：1
     */
    @ApiModelProperty(value = "验证码类型：邮件：0， 短信：1")
    private Integer type;
    /***
     * 验证码 id
     */
    @ApiModelProperty(value = "验证码 id")
    private String key;
    /***
     * 验证码
     */
    @ApiModelProperty(value = "用户提交验证码")
    private String code;
}
