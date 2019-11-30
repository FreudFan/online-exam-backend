package service;

import model.TopicFile;
import utils.ExcelUtils;

import java.io.File;
import java.util.List;

public class TopticsService {

    /***
     * 解析excel文件内容
     * @param topicFiles
     * @throws Exception
     */
    public void readTopicExcel( List<TopicFile> topicFiles ) throws Exception {
        for ( TopicFile topicFile: topicFiles ) {
            File file = topicFile.getFile();
            List<List<Object>> listList = ExcelUtils.readExcel(file);
        }

    }
}
