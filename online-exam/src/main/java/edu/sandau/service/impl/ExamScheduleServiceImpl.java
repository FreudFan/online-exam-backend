package edu.sandau.service.impl;

import edu.sandau.dao.ExamScheduleDao;
import edu.sandau.entity.ExamSchedule;
import edu.sandau.service.ExamScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {
    @Autowired
    private ExamScheduleDao examScheduleDao;

    @Override
    public ExamSchedule saveOrUpdate(ExamSchedule examSchedule) {
        if(examSchedule.getId() == null) {
            return examScheduleDao.save(examSchedule);
        }
        examScheduleDao.update(examSchedule);
        return examScheduleDao.getExamScheduleById(examSchedule.getId());
    }

}
