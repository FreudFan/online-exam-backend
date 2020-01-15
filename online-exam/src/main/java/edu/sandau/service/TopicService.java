package edu.sandau.service;

import edu.sandau.entity.Topic;
import edu.sandau.entity.UploadFile;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.TopicData;


import java.io.InputStream;
import java.util.List;

public interface TopicService {

    Page getTopicByPage(Page page);

    TopicData readTopicExcel(InputStream fileInputStream, String fileName) throws Exception;

    /***
     * 判断导入的题目的类型
     * @param data
     * @return
     */
    List<List<Object>> checkTopicType(List<List<Object>> data);

    UploadFile getFileById(Integer id) throws Exception;

    int getChooseCount( List<Object> titleList);

    void deleteTopics(String idName, List<Integer> idArrays);

    int save(TopicData data);

    void insertTopics(List<Topic> topicList);

    void updateTopics(Topic topic);
}
