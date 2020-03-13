package edu.sandau.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "exercise")
public class ApplicationProperties {

    private TimeoutProperties timeout;

    private WechatAccountProperties wechat;
}
