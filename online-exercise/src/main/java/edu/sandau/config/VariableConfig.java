package edu.sandau.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "exercise")
public class VariableConfig {
    /** redis-session的生存时间 */
    public Integer session_timeout;

    /** 邮件验证码缓存时间 */
    public Integer mail_timeout;

    /** 短信验证码缓存时间 */
    public Integer sms_timeout;
}
