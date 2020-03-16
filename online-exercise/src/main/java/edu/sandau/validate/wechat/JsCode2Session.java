package edu.sandau.validate.wechat;

import lombok.Data;

@Data
public class JsCode2Session {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode = 0;
    private String errmsg;
}
