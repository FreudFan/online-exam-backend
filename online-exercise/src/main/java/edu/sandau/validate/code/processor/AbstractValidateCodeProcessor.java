package edu.sandau.validate.code.processor;

import edu.sandau.security.SessionUtils;
import edu.sandau.utils.JacksonUtil;
import edu.sandau.validate.code.ValidateCode;
import edu.sandau.validate.code.ValidateCodeParam;
import edu.sandau.validate.code.generator.ValidateCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;
    @Autowired
    private SessionUtils sessionUtils;

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
        Map<String, String> param = new HashMap<>(1);
        param.put(getSessionKey(request), JacksonUtil.toJSON(validateCode));
        sessionUtils.setAttribute(param);
    }

    private String getSessionKey(HttpServletRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    @Override
    public Boolean validate(HttpServletRequest request, ValidateCodeParam validateCodeParam) {
        String sessionKey = getSessionKey(request);
        ValidateCode codeInSession = sessionUtils.getAttribute(sessionKey, ValidateCode.class);
        if (codeInSession != null && codeInSession.isNotExpired()) {
            String code = validateCodeParam.getCode();
            if (StringUtils.equals(codeInSession.getCode(), code)) {
                sessionUtils.removeAttribute(sessionKey);
                return true;
            }
        } else {
            sessionUtils.removeAttribute(sessionKey);
        }
        return false;
    }

    protected abstract void send(HttpServletRequest request, ValidateCode validateCode, ValidateCodeParam validateCodeParam) throws Exception;
}
