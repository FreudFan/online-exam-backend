package edu.sandau.validate.code.processor;

import edu.sandau.validate.code.ValidateCode;
import edu.sandau.validate.code.ValidateCodeParam;
import edu.sandau.validate.code.generator.ValidateCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;
    @Autowired
    private HttpSession httpSession;

    @Override
    public void create(HttpServletRequest request, ValidateCodeParam validateCodeParam) throws Exception {
        ValidateCode validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode, validateCodeParam);
    }

    private ValidateCode generate(HttpServletRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        String generatorName = type.concat("CodeGenerator");
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            log.error("验证码生成器" + generatorName + "不存在");
        }
        return validateCodeGenerator.generate(request);
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    private void save(HttpServletRequest request, ValidateCode validateCode) {
        httpSession.setAttribute(getSessionKey(request), validateCode);
    }

    private String getSessionKey(HttpServletRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    @Override
    public Boolean validate(HttpServletRequest request, ValidateCodeParam validateCodeParam) {
        String sessionKey = getSessionKey(request);
        ValidateCode codeInSession = (ValidateCode)httpSession.getAttribute(sessionKey);
        if(codeInSession != null && codeInSession.isNotExpired()) {
            String code = validateCodeParam.getCode();
            if (StringUtils.equals(codeInSession.getCode(), code)) {
                httpSession.removeAttribute(sessionKey);
                return true;
            }
        } else {
            httpSession.removeAttribute(sessionKey);
        }
        return false;
    }

    protected abstract void send(HttpServletRequest request, ValidateCode validateCode, ValidateCodeParam validateCodeParam) throws Exception;
}
