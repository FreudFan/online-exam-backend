package edu.sandau.utils;

import freemarker.template.*;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtil {

    private static String PROJECT_PATH = System.getProperty("user.dir");// 工程物理的绝对路径
    private static String WEB_APP_PATH = PROJECT_PATH + File.separatorChar
            + "online-exam/src/main/resources/template";

    /***
     * 读取freemarker模板转成字符串
     * @param fileName
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String getTemplate(String fileName, Map<String,Object> model) throws IOException, TemplateException {
        //2.创建一个Configuration对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //3.设置模版文件的保存目录
        configuration.setDirectoryForTemplateLoading(new File(WEB_APP_PATH));  //必须使用绝对路径
        //4.模版文件的编码方式，一般就是utf-8
        configuration.setDefaultEncoding("utf-8");
        //5.加载一个模版文件，创建一个模版对象。
        Template template = configuration.getTemplate(fileName);
        //6.创建一个数据集，可以是pojo可以是map，推荐使用map
        model.put("code", "23333");

        // 合并数据模型与模板
        Writer out = new StringBuilderWriter();
        template.process(model, out); //将合并后的数据和模板写入到流中，这里使用的字符流
        out.flush();
        return out.toString();
    }

}
