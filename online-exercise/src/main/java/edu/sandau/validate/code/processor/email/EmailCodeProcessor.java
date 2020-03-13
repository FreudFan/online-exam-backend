package edu.sandau.validate.code.processor.email;

import edu.sandau.validate.code.ValidateCode;
import edu.sandau.validate.code.ValidateCodeParam;
import edu.sandau.validate.code.processor.AbstractValidateCodeProcessor;
import edu.sandau.validate.code.processor.ValidateCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class EmailCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {
    @Autowired
    private ValidateCodeSender emailCodeSender;

    @Override
    protected void send(HttpServletRequest request, ValidateCode validateCode, ValidateCodeParam validateCodeParam) throws Exception {
        String emailAddress = validateCodeParam.getEmail();
        emailCodeSender.send(emailAddress, validateCode.getCode());
    }
}
