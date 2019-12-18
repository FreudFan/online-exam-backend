package edu.sandau.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送邮件时，接收参数的类
 */
@Data
public class EmailVo implements Serializable {
    // 收件人
    private String tos;
    //邮件主题
    private String subject;
    //邮件正文
    private String content;
}
