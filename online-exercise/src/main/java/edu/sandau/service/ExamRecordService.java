package edu.sandau.service;

import edu.sandau.entity.ExamRecord;
import edu.sandau.entity.Subject;
import edu.sandau.rest.model.exam.ExamDetailAndWorryTopic;
import edu.sandau.rest.model.exam.ExamRecordAndExamDeatil;
import edu.sandau.rest.model.exam.ExamTopic;

import java.util.List;
import java.util.Map;

public interface ExamRecordService {

    /***
     * 根据recordId查看这条做题记录的所有详情,包括错题信息，题目信息
     * @param recordId
     * @return
     */
    List<ExamDetailAndWorryTopic> findDetailByRecordId(Integer recordId);

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
    Double endExam(ExamTopic examTopic) throws Exception;

    /***
     * 更新所有题目
     * @param examTopic
     * @return
     * @throws Exception
     */
    void refreshRecord(ExamTopic examTopic) throws Exception;

    /***
     * 修改成绩
     * @param id
     * @param score
     */
    void updateScoreById(Integer id, Double score);

    /***
     * 查找用户所有的做题记录
     * @param subjectId
     * @param userId
     * @return
     */

    List<ExamRecordAndExamDeatil> findAll(Integer subjectId, Integer userId);

    /***
     * 返回用户做过的课程号以及课程名列表
     * @param userId
     * @return
     */
    List<Subject> getSubjectByUserId(Integer userId);
}
