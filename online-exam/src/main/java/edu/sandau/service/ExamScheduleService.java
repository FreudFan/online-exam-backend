package edu.sandau.service;

import edu.sandau.entity.ExamSchedule;

public interface ExamScheduleService {

    /***
     * 新增或更新日程
     * @param examSchedule
     * @return
     */
    ExamSchedule saveOrUpdate(ExamSchedule examSchedule);

}
