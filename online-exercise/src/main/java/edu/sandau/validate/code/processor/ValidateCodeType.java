package edu.sandau.validate.code.processor;

import edu.sandau.validate.code.ValidateConstants;

public enum ValidateCodeType {
    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return ValidateConstants.SMS_CODE;
        }
    },
    /**
     * 图片验证码
     */
    EMAIL {
        @Override
        public String getParamNameOnValidate() {
            return ValidateConstants.EMAIL_CODE;
        }
    };

    public abstract String getParamNameOnValidate();

}
