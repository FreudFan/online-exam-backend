package edu.sandau.service;

import edu.sandau.entity.Exam;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;

import java.util.List;

public interface ExamService {

    /***
     * 保存用户自定义事件
     * @param exam
     */
    void saveExam(Exam exam);


    /***
     * 分页展示所有试卷
     * @param page
     * @param flag
     * @return
     */
    Page getExamByPage(Page page, int flag);


    /***
     * 得到试卷的详细题目
     * @param id
     * @param role
     * @return
     */
    List<Topic> getExamDetail(Integer id, Integer role);



    /***
     * 禁用指定试卷
     * @param id
     */
    void deleteExam(Integer id);
}
