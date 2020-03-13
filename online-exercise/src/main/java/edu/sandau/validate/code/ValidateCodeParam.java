package edu.sandau.validate.code;

import lombok.Data;

import java.io.Serializable;

/***
 * 验证码接口参数
 */
@Data
public class ValidateCodeParam implements Serializable {
    private String email;
    private String mobile;
    private String code;
}
