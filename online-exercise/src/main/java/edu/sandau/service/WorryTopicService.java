package edu.sandau.service;

import edu.sandau.entity.WorryTopic;
import edu.sandau.rest.model.Page;

import java.util.List;

public interface WorryTopicService {

    /***
     * 保存错题
     * @param worryTopicList
     */
    void saveWorryTopic(List<WorryTopic> worryTopicList);

    /***
     * 根据主键id查找错题表,以判断之前是否已存在相同的错题记录
     * @param userId
     * @param topicId
     * @return
     */
    WorryTopic findWorryTopicByUserIdAndTopicId(Integer userId, Integer topicId);

    /***
     * 删除错题记录
     * @param id
     */
    void deleteWorryTopicByRecordId(Integer id, Integer userId);

    /***
     * 分页查询错题
     * @param page
     * @return
     */
    Page getWorryTopicByPage(Page page);

    WorryTopic findWorryTopicByRecordId(Integer record_id,Integer topic_id);
}
