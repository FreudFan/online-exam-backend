package edu.sandau.utils;

import freemarker.template.*;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class FreemarkerUtil {

    @Value("${resources.dir}")
    private String dir;
    private String PROJECT_PATH = System.getProperty("user.dir");// 工程物理的绝对路径

    /***
     * 读取freemarker模板转成字符串
     * @param fileName
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String getTemplate(String fileName, Map<String,Object> model) throws IOException, TemplateException {
        ClassPathResource resource = new ClassPathResource("template");
        String templatePath = URLDecoder.decode(resource.getURL().toString().substring(6), "UTF-8");
        //2.创建一个Configuration对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //3.设置模版文件的保存目录
        configuration.setDirectoryForTemplateLoading(new File(templatePath));  //必须使用绝对路径
        //4.模版文件的编码方式，一般就是utf-8
        configuration.setDefaultEncoding("utf-8");
        //5.加载一个模版文件，创建一个模版对象。
        Template template = configuration.getTemplate(fileName);
        //6.创建一个数据集，可以是pojo可以是map，推荐使用map

        // 合并数据模型与模板
        Writer out = new StringBuilderWriter();
        template.process(model, out); //将合并后的数据和模板写入到流中，这里使用的字符流
        out.flush();
        return out.toString();
    }

}
