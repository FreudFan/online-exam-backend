package edu.sandau.validate.wechat;

import edu.sandau.config.properties.ApplicationProperties;
import edu.sandau.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/***
 * 微信小程序
 */
@Slf4j
@Component
public class WechatAppHolder {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private RestTemplate wxRestTemplate;

    /***
     * 登录
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     * @param code
     * @return
     */
    public JsCode2Session login(String code) {
        String URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}" +
                "&js_code={code}&grant_type=authorization_code";
        Map<String, String> param = new HashMap<>(3);
        param.put("appId", applicationProperties.getWechat().getAppId());
        param.put("appSecret", applicationProperties.getWechat().getAppSecret());
        param.put("code", code);
        JsCode2Session jscode2session = wxRestTemplate.getForObject(URL, JsCode2Session.class, param);
        if (jscode2session != null && jscode2session.getErrcode() == 0) {
            log.info("微信用户 openId : {} 上线", jscode2session.getOpenid());
            return jscode2session;
        } else {
            log.error("调用微信登录接口异常 JsCode2Session : {}", JacksonUtil.toJSON(jscode2session));
            throw new RuntimeException("微信服务调用失败");
        }
    }
}
