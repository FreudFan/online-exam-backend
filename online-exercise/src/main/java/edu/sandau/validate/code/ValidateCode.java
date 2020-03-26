package edu.sandau.validate.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public ValidateCode() {
    }

    public ValidateCode(String code, Integer expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusMinutes(expireIn);
    }

    @JsonIgnore
    public boolean isNotExpired() {
        return LocalDateTime.now().isBefore(expireTime);
    }

}
