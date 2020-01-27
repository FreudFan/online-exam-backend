package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/***
 * 考试日程
 */
@Data
public class ExamSchedule {
    private Integer id;
    /***
     * 试卷id
     */
    private Integer examId;
    /***
     * 日程开始时间
     */
    private Date beginTime;
    /***
     * 日程结束时间
     */
    private Date endTime;
    /***
     * 日程类型
     */
    private Integer type;
    /***
     * 设置用户
     */
    private Integer setterId;
    /***
     * 日程介绍
     */
    private String description = "";
    /***
     * '日程是否可用 0:不可用 1:可用'
     */
    private Integer flag;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
