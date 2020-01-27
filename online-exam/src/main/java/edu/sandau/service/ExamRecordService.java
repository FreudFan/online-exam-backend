package edu.sandau.service;

import edu.sandau.rest.model.exam.ExamTopic;

public interface ExamRecordService {

    /***
     * 用户做题实时保存，做一道存一道
     * @param examTopic
     * @return
     * @throws Exception
     */
    Boolean saveTopic(ExamTopic examTopic) throws Exception;
}
