package edu.sandau.config;

import edu.sandau.validate.wechat.WxMappingJackson2HttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BeanConfig {

    @Bean
    RestTemplate wxRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

}
