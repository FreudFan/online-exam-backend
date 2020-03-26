package edu.sandau.service.impl;

import edu.sandau.entity.EmailMessage;
import edu.sandau.service.EmailService;
import edu.sandau.utils.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /***
     * 发送邮箱地址 FROM
     */
    @Value("${spring.mail.username}")
    private String USERNAME;

    public void sendSimpleMail(EmailMessage emailMessage) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(USERNAME);
            //用于接收邮件的邮箱
            simpleMailMessage.setTo(emailMessage.getEmail());
            //邮件的主题
            simpleMailMessage.setSubject(emailMessage.getSubject());
            //邮件的正文，第二个boolean类型的参数代表html格式
            simpleMailMessage.setText(emailMessage.getContent());
            //发送邮件
            mailSender.send(simpleMailMessage);
            log.info("发送邮件：{}", simpleMailMessage);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }

    public void sendHTMLMail(EmailMessage emailMessage, Map<String,Object> model, String templateFileName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            mimeMessage.setFrom(new InternetAddress(USERNAME));
//            mimeMessage.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(USERNAME));
//            mimeMessage.addRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailMessage.getEmail()));
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            messageHelper.setFrom(new InternetAddress(USERNAME));
            messageHelper.setTo(InternetAddress.parse(emailMessage.getEmail()));   //设定收件人Email
            messageHelper.setCc(InternetAddress.parse(USERNAME));
            messageHelper.setSubject(emailMessage.getSubject());    //设置邮件主题
            String text = FreemarkerUtil.getTemplate(templateFileName, model);
            messageHelper.setText(text, true);  //设置邮件主题内容
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
    }

}