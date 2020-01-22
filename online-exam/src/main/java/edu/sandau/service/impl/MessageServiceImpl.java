package edu.sandau.service.impl;

import edu.sandau.entity.EmailMessage;
import edu.sandau.service.EmailService;
import edu.sandau.service.MessageService;
import edu.sandau.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private EmailService emailService;
    /** 邮件验证码缓存时间 */
    @Value("${mail.timeout}")
    private Integer MAIL_TIME_OUT;
    /** 短信验证码缓存时间 */
    @Value("${sms.timeout}")
    private Integer SMS_TIME_OUT;

    public String sendEmailVerification(String email) {
        //随机6位数
        int code = (int) ((Math.random()*9+1) * 100000);
        String uuid = RedisUtil.createKey();
        EmailMessage message = new EmailMessage(email, "验证码", null);
        Map<String, Object> model = new HashMap<>(1);
        model.put("code", code);
        try {
            emailService.sendHTMLMail(message, model, "email_code.ftl");
            redisTemplate.opsForValue().set(uuid, String.valueOf(code));
            redisTemplate.expire(uuid, MAIL_TIME_OUT, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return uuid;
    }
}
