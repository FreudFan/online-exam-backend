package edu.sandau.validate.code.processor.sms;

import edu.sandau.validate.code.processor.ValidateCodeSender;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeSender implements ValidateCodeSender {

    @Override
    public void send(String phone, String code) {
        System.out.println("发送短信验证码 " + phone + " : " + code);
    }
}
