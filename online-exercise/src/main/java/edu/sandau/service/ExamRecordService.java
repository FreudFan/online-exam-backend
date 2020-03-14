package edu.sandau.service;

import edu.sandau.entity.ExamRecord;
import edu.sandau.rest.model.exam.ExamTopic;

import java.util.Map;

public interface ExamRecordService {

    /***
     * 开始做题, 新增一条做题记录
     * @param examId
     * @return
     * @throws Exception
     */
    Map<String, Object> startExam(Integer examId) throws Exception;



    /***
     * 添加做题记录
     * @param examId
     * @return
     * @throws Exception
     */
    ExamRecord addRecord(Integer examId) throws Exception;



    /***
     * 用户做题实时保存，做一道存一道
     * @param examTopic
     * @return
     * @throws Exception
     */
    Boolean saveOrUpdateTopic(ExamTopic examTopic) throws Exception;



    /***
     * 结束做题, 更新所有题目答案
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


}
