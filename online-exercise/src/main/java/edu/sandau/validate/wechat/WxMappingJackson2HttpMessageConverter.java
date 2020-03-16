package edu.sandau.validate.wechat;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public WxMappingJackson2HttpMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<>(1);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        super.setSupportedMediaTypes(mediaTypes);
    }
}
