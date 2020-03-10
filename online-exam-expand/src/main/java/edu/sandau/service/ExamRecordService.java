package edu.sandau.service;

import edu.sandau.entity.ExamRecord;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.exam.ExamTopic;

import java.util.Map;

public interface ExamRecordService {

    /***
     * 用户做题实时保存，做一道存一道
     * @param examTopic
     * @return
     * @throws Exception
     */
    Boolean saveOrUpdateTopic(ExamTopic examTopic) throws Exception;

    /***
     * 开始考试, 新增一条考试记录
     * @param scheduleId
     * @return
     * @throws Exception
     */
    Map<String, Object> startExam(Integer scheduleId) throws Exception;

    /***
     * 添加考试记录
     * @param scheduleId
     * @return
     * @throws Exception
     */
    ExamRecord addRecord(Integer scheduleId) throws Exception;

    /***
     * 查询用户是否有在进行中的考试，若有，返回考试记录
     * @param scheduleId
     * @return
     * @throws Exception
     */
    ExamRecord checkRecord(Integer scheduleId) throws Exception;

    /***
     * 结束考试, 更新所有题目答案
     * @param examTopic
     * @return
     * @throws Exception
     */
    Boolean endExam(ExamTopic examTopic) throws Exception;

    /***
     * 更新所有题目
     * @param examTopic
     * @return
     * @throws Exception
     */
    void refreshRecord(ExamTopic examTopic) throws Exception;

    /***
     * 分页查询用户考试记录
     * @param userId
     * @return
     * @throws Exception
     */
    Page findRecords(Integer userId, Page page) throws Exception;
}
