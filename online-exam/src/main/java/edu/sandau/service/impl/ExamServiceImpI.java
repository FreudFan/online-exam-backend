package edu.sandau.service.impl;

import edu.sandau.dao.ExamDao;
import edu.sandau.entity.Exam;
import edu.sandau.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpI implements ExamService {

    @Autowired
    private ExamDao examDao;
    @Override
    public void saveExam(Exam exam) {
        exam = examDao.save(exam);
        examDao.saveDetail(exam);
    }
}
