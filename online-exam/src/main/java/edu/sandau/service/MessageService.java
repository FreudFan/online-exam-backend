package edu.sandau.service;

public interface MessageService {

    /***
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @return
     */
    String sendEmailVerification(String email);
}
