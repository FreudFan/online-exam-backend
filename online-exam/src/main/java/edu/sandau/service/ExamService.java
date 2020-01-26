package edu.sandau.service;

import edu.sandau.entity.Exam;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;

import java.util.List;

public interface ExamService {

    void saveExam(Exam exam);

    Page getExamByPage(Page page, int flag);

    List<Topic> getExamDetail(Integer id,Integer role);

    void deleteExam(Integer id);
}
