package edu.sandau.config.properties;

import lombok.Data;

@Data
public class TimeoutProperties {
    /** redis-session的生存时间 ttl 分钟 */
    public Integer redis;
    /** 邮件验证码有效期 分钟 */
    public Integer mail;
    /** 短信验证码有效期 分钟 */
    public Integer sms;
}
