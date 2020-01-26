package edu.sandau.service;

import edu.sandau.entity.Exam;
import edu.sandau.rest.model.exam.ExamModel;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;

import java.util.List;

public interface ExamService {

    void saveExam(Exam exam);

    Page getExamByPage(Page page, int flag);

    List<Topic> getExamDetail(Integer id,Integer role);

    void deleteExam(Integer id);

    /***
     * 自动生成试卷
     * @param examModel
     * @return
     */
    Object autoGenerate(ExamModel examModel);
}
