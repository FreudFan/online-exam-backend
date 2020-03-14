package edu.sandau.service.impl;

import edu.sandau.dao.ExamRecordDao;
import edu.sandau.dao.ExamRecordTopicDao;
import edu.sandau.entity.*;
import edu.sandau.rest.model.exam.ExamDetailAndWorryTopic;
import edu.sandau.rest.model.exam.ExamRecordAndExamDeatil;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.RequestContent;
import edu.sandau.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ExamRecordServiceImpl implements ExamRecordService {

    @Autowired
    private ExamService examService;
    @Autowired
    private ExamRecordDao examRecordDao;
    @Autowired
    private ExamRecordTopicDao examRecordTopicDao;
    @Autowired
    private OptionService optionService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private WorryTopicService worryTopicService;

    @Override
    public List<ExamDetailAndWorryTopic> findDetailByRecordId(Integer recordId) {
        List<ExamDetailAndWorryTopic> examDetails = examRecordTopicDao.findDetailByRecordId(recordId);
        examDetails.stream().forEach((examDetailAndWorryTopic)->{
            Integer topic_id = examDetailAndWorryTopic.getTopic_id();
            List<Option> options = optionService.findOptionById(topic_id);
            examDetailAndWorryTopic.setOptionList(options);
            WorryTopic worryTopic = worryTopicService.findWorryTopicByRecordId(examDetailAndWorryTopic.getRecord_id(), examDetailAndWorryTopic.getTopic_id());
            if(worryTopic != null){
                examDetailAndWorryTopic.setWorryanswer(worryTopic.getWorryanswer());
                examDetailAndWorryTopic.setWorrycount(worryTopic.getWorrycount());
            }
        });
        return examDetails;
    }

    @Override
    public Map<String, Object> startExam(Integer examId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        //获取此次做题的题目
        List<Topic> topicsList = examService.getExamDetail(examId,0);
        ExamRecord examRecord = this.addRecord(examId);
        param.put("recordId", examRecord.getId());
        param.put("topics", topicsList);

        return param;
    }


    @Override
    public ExamRecord addRecord(Integer examId) throws Exception {
        //添加做题记录表，开始做题
        int userId = RequestContent.getCurrentUser().getId();
        Date beginTime = Calendar.getInstance().getTime();
        ExamRecord record = new ExamRecord(userId, examId, beginTime);
        return examRecordDao.save(record);
    }

    @Override
    public Boolean saveOrUpdateTopic(ExamTopic examTopic) throws Exception {
        //查询记录表中是否已存在此题
        ExamRecordTopic recordTopic = examRecordTopicDao.getRecordTopicByElements(examTopic.getRecordId(), examTopic.getTopicId());
        if(recordTopic == null) {
            //不存在，存入做题详情表
            recordTopic = new ExamRecordTopic(examTopic.getRecordId(), examTopic.getTopicId(), examTopic.getAnswer());
            examRecordTopicDao.save(recordTopic);
        } else {
            //如存在，重新修改答案
            String answer = examTopic.getAnswer();
            Integer recordId = examTopic.getRecordId();
            Integer topicId = examTopic.getTopicId();
            examRecordTopicDao.updateByRecordIdAndTopicId(answer, recordId, topicId);
        }
        return true;
    }

    @Override
    public Double endExam(ExamTopic examTopic) throws Exception {
        //记录提交时间
        Date endTime = Calendar.getInstance().getTime();
        ExamRecord record = examRecordDao.getRecordById(examTopic.getRecordId());
        if(record.getEndTime() == null) {
            record.setEndTime(endTime);
        }
        examRecordDao.updateEndTimeById(record);
        //重置所有答案
        this.refreshRecord(examTopic);
        Double score = examService.makeStandardExam(record);
        return score;
    }

    @Override
    public void refreshRecord(ExamTopic examTopic) throws Exception {
        //删除原答题详情，将用户最终提交的答题结果存入表中
        examRecordTopicDao.deleteByRecordId(examTopic.getRecordId());
        examRecordTopicDao.saveBatch(examTopic);
    }

    @Override
    public void updateScoreById(Integer id, Double score) {
        examRecordDao.updateScoreById(id,score);
    }

    @Override
    public List<ExamRecordAndExamDeatil> findAll(Integer subjectId,Integer userId) {
        return examRecordDao.findAll(subjectId,userId);
    }

    @Override
    public List<Subject> getSubjectByUserId(Integer userId) {
        List<Subject> subjects = examRecordDao.getSubjectIdByUserId(userId);
        List<Subject> subjectList = new ArrayList<>();
        subjects.stream().forEach((subject) -> {
            try {
                Subject s = subjectService.getSubjectById(subject.getId());
                subjectList.add(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return subjectList;
    }


}
