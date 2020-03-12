package edu.sandau.service.impl;

import edu.sandau.dao.ExamDao;
import edu.sandau.entity.Exam;
import edu.sandau.entity.ExamDetail;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import edu.sandau.service.ExamService;
import edu.sandau.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExamServiceImpl implements ExamService  {

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
    public List<Topic> getExamDetail(Integer id, Integer role) {
        List<ExamDetail> examDetails = examDao.listExamDetail(id);
        List<Integer> ids = new ArrayList<Integer>();
        examDetails.stream().forEach((examDetail)->{
            ids.add(examDetail.getTopic_id());
        });
        List<Topic> topics = topicService.getTopicById(ids,role);
        int i = 0;
        topics.stream().forEach((topic)->{
            for (ExamDetail ed : examDetails)  {
                if(ed.getTopic_id().intValue() == topic.getId().intValue()){
                    topic.setTopicmark(ed.getTopicmark());
                    break;
                }
            }
        });
        return topics;
    }

    @Override
    public void deleteExam(Integer id) {
        examDao.deleteExam(id);
    }
}
