package edu.sandau.service;

import edu.sandau.entity.ExamSchedule;

public interface ExamScheduleService {

    /***
     * 新增或更新日程
     * @param examSchedule
     * @return
     */
    ExamSchedule saveOrUpdate(ExamSchedule examSchedule);

    /***
     * 删除日程，已有用户参与的不允许删除
     * @param examSchedule
     * @return
     */
    Boolean deleteSchedule(ExamSchedule examSchedule);

    /***
     * 禁用日程
     * @param examSchedule
     */
    void clockSchedule(ExamSchedule examSchedule);

}
