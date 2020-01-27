package edu.sandau.service.impl;

import edu.sandau.dao.ExamRecordDao;
import edu.sandau.dao.ExamRecordTopicDao;
import edu.sandau.dao.ExamScheduleDao;
import edu.sandau.entity.ExamRecord;
import edu.sandau.entity.ExamRecordTopic;
import edu.sandau.entity.ExamSchedule;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.ExamRecordService;
import edu.sandau.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class ExamRecordServiceImpl implements ExamRecordService {
    @Autowired
    private ExamRecordDao examRecordDao;
    @Autowired
    private ExamService examService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ExamScheduleDao examScheduleDao;
    @Autowired
    private ExamRecordTopicDao examRecordTopicDao;

    @Override
    public Boolean saveOrUpdateTopic(ExamTopic examTopic) throws Exception {
        ExamRecordTopic recordTopic = examRecordTopicDao.getRecordTopicByElements(examTopic.getRecordId(), examTopic.getTopicId());
        if(recordTopic == null) {
            recordTopic = new ExamRecordTopic(examTopic.getRecordId(), examTopic.getTopicId(), examTopic.getAnswer());
            examRecordTopicDao.save(recordTopic);
        } else {
            String answer = examTopic.getAnswer();
            Integer recordId = examTopic.getRecordId();
            Integer topicId = examTopic.getTopicId();
            examRecordTopicDao.updateByRecordIdAndTopicId(answer, recordId, topicId);
        }
        return true;
    }

    @Override
    public Map<String, Object> startExam(Integer scheduleId) throws Exception {
        ExamSchedule schedule = examScheduleDao.getExamScheduleById(scheduleId);
        List<Topic> topicsList = examService.getExamDetail(schedule.getExamId(),0);
        ExamRecord record = this.addRecord(scheduleId);
        Map<String, Object> param = new HashMap<>();
        param.put("recordId", record.getId());
        param.put("topics", topicsList);
        return param;
    }

    @Override
    public ExamRecord addRecord(Integer scheduleId) throws Exception {
        //添加考试记录表，开始考试
        int userId = sessionWrapper.getUserId();
        Date beginTime = Calendar.getInstance().getTime();
        ExamRecord record = new ExamRecord(userId, scheduleId, beginTime);
        return examRecordDao.save(record);
    }

    @Override
    public Boolean endExam(ExamTopic examTopic) throws Exception {
        //设置考试结束时间
        Date endTime = Calendar.getInstance().getTime();
        ExamRecord record = examRecordDao.getExamRecordById(examTopic.getRecordId());
        if(record.getEndTime() == null) {
            record.setEndTime(endTime);
        }
        examRecordDao.updateById(record);
        //重置所有答案
        this.refreshRecord(examTopic);
        return true;
    }

    @Override
    public void refreshRecord(ExamTopic examTopic) throws Exception {
        examRecordTopicDao.deleteByRecordId(examTopic.getRecordId());
        examRecordTopicDao.saveBatch(examTopic);
    }

}
