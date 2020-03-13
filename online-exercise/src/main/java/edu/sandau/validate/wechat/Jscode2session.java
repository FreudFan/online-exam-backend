package edu.sandau.validate.wechat;

import lombok.Data;

@Data
public class Jscode2session {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
