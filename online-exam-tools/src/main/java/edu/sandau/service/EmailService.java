package edu.sandau.service;

import edu.sandau.model.EmailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    /***
     * 发送邮件
     * @param emailVo 邮箱发送对象
     * @throws Exception
     */
    public void send(EmailVo emailVo) throws Exception {
        try {
            //用于接收邮件的邮箱
            simpleMailMessage.setTo(emailVo.getTos());
            //邮件的主题
            simpleMailMessage.setSubject(emailVo.getSubject());
            //邮件的正文，第二个boolean类型的参数代表html格式
            simpleMailMessage.setText(emailVo.getContent());

            log.info("发送邮件：{}", simpleMailMessage);
            //发送
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new MessagingException("failed to send mail!", e);
        }
    }

    /***
     * 手动设置邮件发送配置
     * @param javaMailSender
     */
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }
}