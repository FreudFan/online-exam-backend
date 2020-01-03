package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送邮件时，接收参数的类
 */
@Data
@NoArgsConstructor
public class EmailMessage implements Serializable {
    @JSONField(serialize = false)
    @Getter private final String TABLE_NAME = "email_message";

    @Getter @Setter
    private Integer email_message_id;
    // 收件人
    @Getter @Setter
    private String email;
    // 发件人id
    @Getter @Setter
    private Integer tosId;
    //邮件主题
    @Getter @Setter
    private String subject;
    //邮件正文
    @Getter @Setter
    private String content;

    @Getter @JSONField(serialize = false)
    private Date createtime;
    @Getter @JSONField(serialize = false)
    private Date updatetime;

    public EmailMessage(String email, String subject, Integer tosId) {
        this.email = email;
        this.subject = subject;
        this.tosId = tosId;
    }
}
