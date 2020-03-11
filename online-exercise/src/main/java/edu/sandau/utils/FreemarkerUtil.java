package edu.sandau.utils;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class FreemarkerUtil {

    private static FreeMarkerConfigurer configurer;

    public FreemarkerUtil(FreeMarkerConfigurer configurer) {
        FreemarkerUtil.configurer = configurer;
    }

    public static String getTemplate(Template template, Map<String, Object> model) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            log.error("读取freemarker模板异常", e);
        }
        return null;
    }

    /***
     * 读取freemarker模板转成字符串
     * @param templateName
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String getTemplate(String templateName, Map<String,Object> model) {
        try {
            Template template = configurer.getConfiguration().getTemplate(templateName);
            return getTemplate(template, model);
        } catch (IOException e) {
            log.error("读取freemarker模板异常", e);
        }
        return null;
    }

}
