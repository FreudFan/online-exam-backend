package edu.sandau.service.impl;

import edu.sandau.dao.ExamRecordDao;
import edu.sandau.dao.ExamScheduleDao;
import edu.sandau.entity.ExamRecord;
import edu.sandau.entity.ExamSchedule;
import edu.sandau.service.ExamScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {
    @Autowired
    private ExamScheduleDao examScheduleDao;
    @Autowired
    private ExamRecordDao examRecordDao;

    @Override
    public ExamSchedule saveOrUpdate(ExamSchedule examSchedule) {
        if(examSchedule.getId() == null) {
            return examScheduleDao.save(examSchedule);
        }
        examScheduleDao.update(examSchedule);
        return examScheduleDao.getAccessExamScheduleById(examSchedule.getId());
    }

    @Override
    public Boolean deleteSchedule(ExamSchedule examSchedule) {
        List<ExamRecord> records = examRecordDao.getRecordsByScheduleId(examSchedule.getId());
        if(!records.isEmpty()) {
            return false;
        }
        examScheduleDao.delete(examSchedule.getId());
        return true;
    }

    @Override
    public void clockSchedule(ExamSchedule examSchedule) {
        examScheduleDao.clock(examSchedule.getId());
    }

}
