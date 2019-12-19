package edu.sandau.service;

import edu.sandau.dao.EmailVoDao;
import edu.sandau.model.EmailMessage;
import edu.sandau.utils.FreemarkerUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private EmailVoDao emailVoDao;

    /***
     * 发送邮件 只能发文字
     * @param emailMessage 邮箱发送对象
     * @throws Exception
     */
    public void sendSimpleMail(EmailMessage emailMessage) throws Exception {
        try {
            //用于接收邮件的邮箱
            simpleMailMessage.setTo(emailMessage.getTos());
            //邮件的主题
            simpleMailMessage.setSubject(emailMessage.getSubject());
            //邮件的正文，第二个boolean类型的参数代表html格式
            simpleMailMessage.setText(emailMessage.getContent());

            //发送邮件
            javaMailSender.send(simpleMailMessage);
            log.info("发送邮件：{}", simpleMailMessage);
//            emailVoDao.save(emailMessage);
        } catch (Exception e) {
            throw new MessagingException("failed to send mail!", e);
        }
    }

    //带附件的HTML格式的Email
    public void sendHTMLMail(EmailMessage emailMessage) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        messageHelper.setSubject(emailMessage.getSubject()); //设置邮件主题
        messageHelper.setText(emailMessage.getContent());   //设置邮件主题内容
        messageHelper.setTo(emailMessage.getTos());          //设定收件人Email

        String text = FreemarkerUtil.getTemplate("email.ftl").toString();
        messageHelper.setText(text, true);
        javaMailSender.send(mimeMessage);
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