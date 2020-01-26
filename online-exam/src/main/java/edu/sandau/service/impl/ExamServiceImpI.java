package edu.sandau.service.impl;

import edu.sandau.dao.ExamDao;
import edu.sandau.entity.Exam;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import edu.sandau.service.ExamService;
import edu.sandau.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpI implements ExamService {

    @Autowired
    private ExamDao examDao;
    @Autowired
    private TopicService topicService;
    @Override
    public void saveExam(Exam exam) {
        exam = examDao.save(exam);
        examDao.saveDetail(exam);
    }

    @Override
    public Page getExamByPage(Page page, int flag) {
        List<Exam> exams = examDao.listExamByPage(page,flag);
        int total = examDao.getCount(flag);
        page.setTotal(total);
        page.setRows(exams);
        return page;
    }

    @Override
    public List<Topic> getExamDetail(Integer id,Integer role) {
        List<Integer> idList = examDao.listExamDetail(id);
        List<Topic> topics = topicService.getTopicById(idList,role);
        return topics;
    }

    @Override
    public void deleteExam(Integer id) {
        examDao.deleteExam(id);
    }
}
