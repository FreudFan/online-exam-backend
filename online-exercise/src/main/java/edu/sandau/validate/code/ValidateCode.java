package edu.sandau.validate.code;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 * 验证码
 */
@Data
public class ValidateCode implements Serializable {
    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(String code, Integer expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusMinutes(expireIn);
    }

    public boolean isNotExpired() {
        return LocalDateTime.now().isBefore(expireTime);
    }

}
