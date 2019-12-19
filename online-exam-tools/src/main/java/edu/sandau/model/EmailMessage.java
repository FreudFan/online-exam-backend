package edu.sandau.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送邮件时，接收参数的类
 */
@Data
public class EmailMessage implements Serializable {
    @JSONField(serialize = false)
    @Getter
    private final String TABLE_NAME = "email_message";

    private Integer email_message_id;
    // 收件人
    private String tos;
    //邮件主题
    private String subject;
    //邮件正文
    private String content;

    @JSONField(serialize = false)
    private @Getter Date createtime;
    @JSONField(serialize = false)
    private @Getter Date updatetime;
}
