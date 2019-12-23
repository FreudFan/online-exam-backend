package edu.sandau.service;

import edu.sandau.model.EmailMessage;
import edu.sandau.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MessageService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EmailService emailService;
    @Value("${mail.timeout}")
    private Integer MAIL_TIME_OUT;
    @Value("${sms.timeout}")
    private Integer SMS_TIME_OUT;

    /***
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @return
     */
    public String sendEmailVerification(String email) {
        int code = (int) ((Math.random()*9+1) * 100000);//随机6位数
        String uuid = redisUtil.createKey();
        EmailMessage message = new EmailMessage(email, "验证码", null);
        Map<String, Object> model = new HashMap<>();
        model.put("code", code);
        try {
            redisTemplate.opsForValue().set(uuid, String.valueOf(code));
            redisTemplate.expire(uuid, MAIL_TIME_OUT, TimeUnit.MINUTES);
            emailService.sendHTMLMail(message, model, "email_code.ftl");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return uuid;
    }
}
