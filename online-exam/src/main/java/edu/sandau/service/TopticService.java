package edu.sandau.service;

import edu.sandau.entity.UploadFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TopticService {

    Map<String, Object> readTopicExcel(InputStream fileInputStream, String fileName) throws Exception;

    UploadFile getFileById(Integer id) throws Exception;

    /***
     * 解析excel文件内容并保存
     * @param uploadFiles
     * @throws Exception
     */
    List<List<List<Object>>> saveTopicExcel(List<UploadFile> uploadFiles) throws Exception;

    int getChooseCount( List<Object> titleList);

    int deleteTopics(String idName, String[] idArrays);

}
