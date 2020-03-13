package edu.sandau.validate.code.generator;

import edu.sandau.config.properties.ApplicationProperties;
import edu.sandau.validate.code.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class EmailCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public ValidateCode generate(HttpServletRequest request) {
        String code = String.valueOf((Math.random()*9+1) * 100000); //默认六位验证码
        return new ValidateCode(code, applicationProperties.getTimeout().getMail());
    }
}
