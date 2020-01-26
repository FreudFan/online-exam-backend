package edu.sandau.service;

import edu.sandau.entity.Exam;
import edu.sandau.rest.model.Page;

public interface ExamService {
    void saveExam(Exam exam);

    Page getExamByPage(Page page, int flag);
}
