package edu.sandau.service;

import edu.sandau.entity.Topic;
import edu.sandau.entity.UploadFile;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.TopicData;
import edu.sandau.rest.model.TopicModel;

import java.io.InputStream;
import java.util.List;

public interface TopicService {

    /***
     *分页查询题目
     * @param page
     * @return
     */
    Page getTopicByPage(Page page, int flag);

    /***
     * 解析excel
     * @param fileInputStream
     * @param fileName
     * @return
     * @throws Exception
     */
    TopicData readTopicExcel(InputStream fileInputStream, String fileName) throws Exception;

    /***
     * 判断导入的题目的类型
     * @param data
     * @return
     */
    List<List<Object>> checkTopicType(List<List<Object>> data);

    /***
     *查询要下载的文件
     * @param id
     * @return
     * @throws Exception
     */
    UploadFile getFileById(Integer id) throws Exception;

    /***
     * 得到上传文件的最大选项列索引
     * @param titleList
     * @return
     */
    int getChooseCount(List<Object> titleList);

    /***
     * 批量禁用题目
     * @param ids
     */
    void deleteTopics(Integer... ids);

    /***
     * 将上传文件的数据存入数据库
     * @param data
     * @return
     */
    int save(TopicData data);

    /***
     * 将用户自定义的题目插入数据库
     * @param topicList
     */
    void insertTopics(List<Topic> topicList);

    /***
     * 更新题目
     * @param topic
     */
    void updateTopics(Topic topic);

    /***
     * 进行字典替换操作
     * @param topics
     * @return
     */
    public List<TopicModel> refactorEntity(List<Topic> topics);

    /***
     * 根据题目id集合查询对应的题目
     * @param ids
     * @param role
     * @return
     */
    List<Topic> getTopicById(List<Integer> ids, Integer role);

    /***
     * 查询题目详细信息
     * @param topics 题目list
     * @return
     */
    List<Topic> getTopicsDetail(List<Topic> topics);

}
