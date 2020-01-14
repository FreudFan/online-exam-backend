package edu.sandau.service;

import edu.sandau.entity.UploadFile;
import edu.sandau.rest.model.TopicData;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TopicService {

    TopicData readTopicExcel(InputStream fileInputStream, String fileName) throws Exception;

    /***
     * 判断导入的题目的类型
     * @param data
     * @return
     */
    List<List<Object>> checkTopicType(List<List<Object>> data);

    UploadFile getFileById(Integer id) throws Exception;

    int getChooseCount( List<Object> titleList);

    int deleteTopics(String idName, String[] idArrays);

    int save(TopicData data);
}
