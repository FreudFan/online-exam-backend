package edu.sandau.validate.code.generator;

import edu.sandau.validate.code.ValidateCode;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeGenerator {

    ValidateCode generate(HttpServletRequest request);
}
