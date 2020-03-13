package edu.sandau.validate.code.processor.email;

import edu.sandau.entity.EmailMessage;
import edu.sandau.service.EmailService;
import edu.sandau.validate.code.processor.ValidateCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailCodeSender implements ValidateCodeSender {
    @Autowired
    private EmailService emailService;

    @Override
    public void send(String email, String code) {
        EmailMessage message = new EmailMessage(email, "验证码", null);
        Map<String, Object> model = new HashMap<>(1);
        model.put("code", code);
        emailService.sendHTMLMail(message, model, "email_code.ftl");
    }
}
