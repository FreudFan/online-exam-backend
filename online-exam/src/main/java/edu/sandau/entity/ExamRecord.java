package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/***
 * 考试记录
 */
@Data
public class ExamRecord {
    private Integer id;
    /***
     * 用户id
     */
    private Integer userId;
    /***
     * 日程id
     */
    private Integer scheduleId;
    /***
     * 考试成绩
     */
    private Double score;
    /***
     * 考试开始时间
     */
    private Date beginTime;
    /***
     * 考试结束时间
     */
    private Date endTime;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
