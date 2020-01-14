package edu.sandau.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送邮件时，接收参数的类
 */
@Data
@NoArgsConstructor
public class EmailMessage implements Serializable {
    private final String TABLE_NAME = "email_message";

    private Integer id;
    /***
     * 收件邮箱地址
     */
    private String email;
    /***
     * 发件人id
     */
    private Integer tosId;
    /***
     * 邮件主题
     */
    private String subject;
    /***
     * 邮件主题
     */
    private String content;

    private Date createtime;
    private Date updatetime;

    public EmailMessage(String email, String subject, Integer tosId) {
        this.email = email;
        this.subject = subject;
        this.tosId = tosId;
    }
}
