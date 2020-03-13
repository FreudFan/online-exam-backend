package edu.sandau.validate.wechat;

import edu.sandau.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/***
 * 微信小程序
 */
@Component
public class WechatAppHolder {
    @Autowired
    private ApplicationProperties applicationProperties;
    private RestTemplate restTemplate = new RestTemplate();

    /***
     * 登录
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     * @param code
     * @return
     */
    public Jscode2session login(String code) {
        String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        String appId = applicationProperties.getWechat().getAppId();
        String secret = applicationProperties.getWechat().getAppSecret();
        URL = String.format(URL, appId, secret, code);
        Jscode2session jscode2session = restTemplate.getForObject(URL, Jscode2session.class);
        if (jscode2session.getErrcode() == 0) {
            return jscode2session;
        } else {
            throw new RuntimeException("微信服务调用失败");
        }
    }
}
