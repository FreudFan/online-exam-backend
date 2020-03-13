package edu.sandau.validate.code.processor;

import edu.sandau.validate.code.ValidateCodeParam;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeProcessor {

    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    void create(HttpServletRequest request, ValidateCodeParam validateCodeParam) throws Exception;

    Boolean validate(HttpServletRequest request, ValidateCodeParam validateCodeParam);
}
