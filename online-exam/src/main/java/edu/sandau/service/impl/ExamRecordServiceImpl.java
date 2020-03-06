package edu.sandau.service.impl;

import edu.sandau.dao.ExamRecordDao;
import edu.sandau.dao.ExamRecordTopicDao;
import edu.sandau.dao.ExamScheduleDao;
import edu.sandau.entity.ExamRecord;
import edu.sandau.entity.ExamRecordTopic;
import edu.sandau.entity.ExamSchedule;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.RequestContent;
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
        Map<String, Object> param = new HashMap<>();
        ExamSchedule schedule = examScheduleDao.getAccessExamScheduleById(scheduleId);
        if (schedule == null) {
            param.put("error", "考试不存在或已禁用");
            return param;
        }
        //检查是否在考试开放时间段内
        Date now = Calendar.getInstance().getTime();
        Date beginTime = schedule.getBeginTime();
        Date endTime = schedule.getEndTime();
        if(now.compareTo(beginTime) < 0) {
            param.put("error", "当前考试未开始");
            return param;
        } else if (endTime != null && now.compareTo(endTime) >= 0) {
            param.put("error", "当前考试已结束");
            return param;
        }
        //获取考试题目
        List<Topic> topicsList = examService.getExamDetail(schedule.getExamId(),0);
        //检查查询考试是否在进行中
        ExamRecord record = this.checkRecord(scheduleId);
        if(record == null) {
            record = this.addRecord(scheduleId);
            param.put("recordId", record.getId());
            param.put("topics", topicsList);
        } else {
            //已完成考试的不允许再次进入考试
            if (record.getEndTime() == null) {
                int recordId = record.getId();
                param.put("recordId", recordId);
                List<ExamRecordTopic> answers = examRecordTopicDao.getRecordAnswersByRecordId(recordId);
                param.put("answer", answers);
                param.put("topics", topicsList);
            } else {
                param.put("error", "您已提交答卷");
            }
        }
        return param;
    }

    @Override
    public ExamRecord addRecord(Integer scheduleId) throws Exception {
        //添加考试记录表，开始考试
        int userId = RequestContent.getCurrentUser().getId();
        Date beginTime = Calendar.getInstance().getTime();
        ExamRecord record = new ExamRecord(userId, scheduleId, beginTime);
        return examRecordDao.save(record);
    }

    @Override
    public ExamRecord checkRecord(Integer scheduleId) throws Exception {
        //查询用户是否有在进行中的考试
        int userId = RequestContent.getCurrentUser().getId();
        return examRecordDao.getRecordByUserIdAndScheduleId(userId, scheduleId);
    }

    @Override
    public Boolean endExam(ExamTopic examTopic) throws Exception {
        //设置考试结束时间
        Date endTime = Calendar.getInstance().getTime();
        ExamRecord record = examRecordDao.getRecordById(examTopic.getRecordId());
        if(record.getEndTime() == null) {
            record.setEndTime(endTime);
        }
        examRecordDao.updateEndTimeById(record);
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
