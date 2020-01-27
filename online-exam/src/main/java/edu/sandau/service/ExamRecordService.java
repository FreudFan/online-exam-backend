package edu.sandau.service;

import edu.sandau.entity.ExamRecord;
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

}
